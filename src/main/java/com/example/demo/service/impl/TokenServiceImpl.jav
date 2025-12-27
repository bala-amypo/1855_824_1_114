package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.entity.Token;
import com.example.demo.entity.TokenLog;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            ServiceCounterRepository counterRepository,
                            TokenLogRepository logRepo,
                            QueuePositionRepository queueRepo) {
        this.tokenRepository = tokenRepository;
        this.counterRepository = counterRepository;
        this.logRepo = logRepo;
        this.queueRepo = queueRepo;
    }

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new IllegalArgumentException("Counter not found"));

        if (counter.getIsActive() == null || !counter.getIsActive()) {
            throw new IllegalArgumentException("Counter not active");
        }

        List<Token> waiting = tokenRepository
                .findByServiceCounter_IdAndStatusOrderByIssuedAtAsc(counterId, "WAITING");

        Token token = new Token();
        token.setServiceCounter(counter);
        token.setStatus("WAITING");
        token.setIssuedAt(LocalDateTime.now());
        token = tokenRepository.save(token);

        QueuePosition qp = new QueuePosition();
        qp.setToken(token);
        qp.setPosition(waiting.size() + 1);
        qp.setUpdatedAt(LocalDateTime.now());
        queueRepo.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(token);
        log.setLogMessage("Token issued");
        log.setLoggedAt(LocalDateTime.now());
        logRepo.save(log);

        return token;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        String current = token.getStatus();
        String next = (status == null) ? "" : status.trim().toUpperCase();

        if (!isValidTransition(current, next)) {
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
        logRepo.save(log);

        return token;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
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
            return current.equals(next);
        }
        return "WAITING".equals(next);
    }
}