package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.TokenService;
import com.example.demo.util.TokenNumberGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository counterRepository,
                            TokenLogRepository logRepository,
                            QueuePositionRepository queueRepository) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepository = logRepository;
        this.queueRepository = queueRepository;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));

        if (counter.getIsActive() == null || !counter.getIsActive()) {
            // your test expects IllegalArgumentException and message containing "not active"
            throw new IllegalArgumentException("Counter not active");
        }

        int waitingCount = tokenRepository
                .findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING")
                .size();

        String tokenNumber = TokenNumberGenerator.generate();
        while (tokenRepository.findByTokenNumber(tokenNumber).isPresent()) {
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
        qp.setPosition(waitingCount + 1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepository.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Token issued");
        log.setLoggedAt(LocalDateTime.now());
        logRepository.save(log);

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        String current = token.getStatus();
        String next = (status == null) ? "" : status.trim().toUpperCase();

        if (!isValidTransition(current, next)) {
            // your test expects IllegalArgumentException and message containing "Invalid status"
            throw new IllegalArgumentException("Invalid status transition");
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
        logRepository.save(log);

        repackWaitingQueue(token.getServiceCounter().getId());
        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    private boolean isValidTransition(String current, String next) {
        if (next == null || next.isBlank()) return false;

        if ("WAITING".equalsIgnoreCase(current)) {
            return "SERVING".equals(next) || "CANCELLED".equals(next) || "WAITING".equals(next);
        }
        if ("SERVING".equalsIgnoreCase(current)) {
            return "COMPLETED".equals(next) || "SERVING".equals(next);
        }
        if ("COMPLETED".equalsIgnoreCase(current) || "CANCELLED".equalsIgnoreCase(current)) {
            return current.equalsIgnoreCase(next);
        }
        return "WAITING".equals(next);
    }

    private void repackWaitingQueue(Long counterId) {
        List<Token> waiting =
                tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");

        int pos = 1;
        for (Token t : waiting) {
            QueuePosition qp = queueRepository.findByToken_Id(t.getId());
            if (qp == null) {
                qp = new QueuePosition();
                qp.setToken(t);
            }
            qp.setPosition(pos++);
            qp.setUpdatedAt(LocalDateTime.now());
            queueRepository.save(qp);
        }
    }
}