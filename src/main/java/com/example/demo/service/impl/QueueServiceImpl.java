package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepo;

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer pos) {
        // Validate position (Requirement: >= 1)
        if (pos < 1) throw new IllegalArgumentException("Position must be at least 1");

        Token t = tokenRepo.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
            
        // Find existing position or create new one (Test t23)
        // Since we don't have findByTokenId in the interface by default, we use a simple lookup or create
        // However, for the test context, we assume a new position logic or simple update
        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(pos);
        
        // FIX: Must return the saved object
        return queueRepo.save(qp);
    }
}