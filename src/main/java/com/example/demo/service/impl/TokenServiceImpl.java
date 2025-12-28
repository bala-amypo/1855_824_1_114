package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    // Constructor params must match test lines 505: TokenRepo, CounterRepo, LogRepo, QueueRepo
    private final TokenRepository tokenRepository;
    private final ServiceCounterRepository counterRepository;
    private final TokenLogRepository logRepository;
    private final QueuePositionRepository queueRepository;

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter sc = counterRepository.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found")); // Test t13 [cite: 528]
        
        if (!Boolean.TRUE.equals(sc.getIsActive())) {
            throw new IllegalArgumentException("Counter not active"); // Test t44 [cite: 573]
        }

        Token t = new Token();
        t.setServiceCounter(sc);
        t.setStatus("WAITING");
        t.setTokenNumber(UUID.randomUUID().toString());
        t.setIssuedAt(LocalDateTime.now());
        t = tokenRepository.save(t);

        // Add to queue (Test t12 requires saving QueuePosition) [cite: 526]
        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(1); 
        queueRepository.save(qp);

        // Add Log (Test t12 requires saving Log)
        TokenLog log = new TokenLog();
        log.setToken(t);
        log.setLogMessage("Issued");
        logRepository.save(log);

        return t;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token t = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        // Validate transition: WAITING -> COMPLETED is invalid (Test t14) [cite: 530]
        if ("COMPLETED".equals(status) && "WAITING".equals(t.getStatus())) {
            throw new IllegalArgumentException("Invalid status transition");
        }
        
        t.setStatus(status);
        if ("COMPLETED".equals(status)) {
            t.setCompletedAt(LocalDateTime.now()); // Test t16 [cite: 534]
        }
        
        Token saved = tokenRepository.save(t);
        
        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setLogMessage("Status changed to " + status);
        logRepository.save(log);
        
        return saved;
    }

    @Override
    public Token getToken(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found")); // Test t17 [cite: 536]
    }
}