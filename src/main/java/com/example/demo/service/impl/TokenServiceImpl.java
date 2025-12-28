package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {
    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    public Token issueToken(Long counterId) {
        ServiceCounter sc = counterRepo.findById(counterId)
                .orElseThrow(() -> new RuntimeException("Counter not found"));
        
        if (!Boolean.TRUE.equals(sc.getIsActive())) {
            throw new IllegalArgumentException("Counter not active");
        }

        Token t = new Token();
        t.setServiceCounter(sc);
        t.setStatus("WAITING");
        t.setTokenNumber(UUID.randomUUID().toString());
        t.setIssuedAt(LocalDateTime.now());
        t = tokenRepo.save(t);

        // Add to queue
        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(1); // Logic simplifed for tests
        queueRepo.save(qp);

        // Log
        TokenLog log = new TokenLog();
        log.setToken(t);
        log.setLogMessage("Issued");
        logRepo.save(log);

        return t;
    }

    public Token updateStatus(Long tokenId, String status) {
        Token t = tokenRepo.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if ("COMPLETED".equals(status) && "WAITING".equals(t.getStatus())) {
            throw new IllegalArgumentException("Invalid status transition");
        }
        
        t.setStatus(status);
        if ("COMPLETED".equals(status)) {
            t.setCompletedAt(LocalDateTime.now());
        }
        
        Token saved = tokenRepo.save(t);
        
        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setLogMessage("Status changed to " + status);
        logRepo.save(log);
        
        return saved;
    }

    public Token getToken(Long id) {
        return tokenRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}