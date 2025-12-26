package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import com.example.demo.util.TokenNumberGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository serviceCounterRepository;
    private final TokenLogRepository tokenLogRepository;
    private final QueuePositionRepository queuePositionRepository;

    // TokenService: (TokenRepository, ServiceCounterRepository, TokenLogRepository, QueuePositionRepository)
    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository serviceCounterRepository,
                            TokenLogRepository tokenLogRepository,
                            QueuePositionRepository queuePositionRepository) {
        this.tokenRepository = tokenRepository;
        this.serviceCounterRepository = serviceCounterRepository;
        this.tokenLogRepository = tokenLogRepository;
        this.queuePositionRepository = queuePositionRepository;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = serviceCounterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (counter.getIsActive() == null || !counter.getIsActive()) {
            throw new IllegalStateException("Counter not active");
        }

        int waitingCount = tokenRepository
                .findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING")
                .size();

        String tokenNumber = TokenNumberGenerator.generate();
        while (tokenRepository.findByTokenNumber(tokenNumber) != null) {
            tokenNumber = TokenNumberGenerator.generate();
        }

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setTokenNumber(tokenNumber);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(waitingCount + 1); // queue position >= 1
        qp.setUpdatedAt(LocalDateTime.now());
        queuePositionRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Token issued");
        log.setLoggedAt(LocalDateTime.now());
        tokenLogRepository.save(log);

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        String current = token.getStatus();
        String next = (status == null) ? "" : status.trim().toUpperCase();

        if (!isValidTransition(current, next)) {
            throw new IllegalStateException("Invalid status transition");
        }

        token.setStatus(next);
        if ("COMPLETED".equals(next)) {
            token.setCompletedAt(LocalDateTime.now());
        }
        token = tokenRepository.save(token);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Status changed to " + next);
        log.setLoggedAt(LocalDateTime.now());
        tokenLogRepository.save(log);

        // Re-pack WAITING queue positions after status changes
        repackWaitingQueue(token.getServiceCounter().getId());

        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    private boolean isValidTransition(String current, String next) {
        if (next.isEmpty()) return false;

        if ("WAITING".equals(current)) {
            return "SERVING".equals(next) || "CANCELLED".equals(next) || "WAITING".equals(next);
        }
        if ("SERVING".equals(current)) {
            return "COMPLETED".equals(next) || "SERVING".equals(next);
        }
        if ("COMPLETED".equals(current) || "CANCELLED".equals(current)) {
            return current.equals(next); // no transitions allowed
        }
        // allow first-time setting
        return "WAITING".equals(next);
    }

    private void repackWaitingQueue(Long counterId) {
        List<Token> waiting = tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");
        int pos = 1;
        for (Token t : waiting) {
            QueuePosition qp = queuePositionRepository.findByToken_Id(t.getId());
            if (qp == null) {
                qp = new QueuePosition();
                qp.setToken(t);
            }
            qp.setPosition(pos++);
            qp.setUpdatedAt(LocalDateTime.now());
            queuePositionRepository.save(qp);
        }
    }
}