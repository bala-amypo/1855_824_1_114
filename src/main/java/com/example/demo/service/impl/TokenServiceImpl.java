package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenService;
import com.example.demo.util.TokenNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepo;
    private final ServiceCounterRepository counterRepo;
    private final TokenLogRepository logRepo;
    private final QueuePositionRepository queueRepo;

    @Override
    public Token issueToken(Long counterId) {
        ServiceCounter sc = counterRepo.findById(counterId)
            .orElseThrow(() -> new ResourceNotFoundException("Counter not found"));
        
        if (!Boolean.TRUE.equals(sc.getIsActive())) 
            throw new CounterNotActiveException("Counter not active");

        Token t = new Token();
        t.setServiceCounter(sc);
        t.setStatus("WAITING");
        // FIX: Calls generate() with NO arguments
        t.setTokenNumber(TokenNumberGenerator.generate());
        t.setIssuedAt(LocalDateTime.now());
        t = tokenRepo.save(t);

        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(1);
        queueRepo.save(qp);

        TokenLog log = new TokenLog();
        log.setToken(t);
        log.setLogMessage("Issued");
        logRepo.save(log);

        return t;
    }

    @Override
    public Token updateStatus(Long tokenId, String status) {
        Token t = getToken(tokenId);
        
        if ("COMPLETED".equals(status) && "WAITING".equals(t.getStatus())) 
            throw new InvalidTokenStatusException("Invalid status transition");
            
        t.setStatus(status);
        
        // FIX: Set completedAt for BOTH COMPLETED and CANCELLED
        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            t.setCompletedAt(LocalDateTime.now());
        }
        
        Token saved = tokenRepo.save(t);
        TokenLog log = new TokenLog();
        log.setToken(saved);
        log.setLogMessage("Status changed to " + status);
        logRepo.save(log);
        return saved;
    }

    @Override
    public Token getToken(Long id) {
        return tokenRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}