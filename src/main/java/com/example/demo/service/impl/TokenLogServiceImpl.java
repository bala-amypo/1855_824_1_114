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
        qp.setPosition(waitingCount + 1);
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

        String next = status == null ? "" : status.trim().toUpperCase();

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

        repackWaitingQueue(token.getServiceCounter().getId());
        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }

    private void repackWaitingQueue(Long counterId) {
        List<Token> waiting =
                tokenRepository.findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");
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