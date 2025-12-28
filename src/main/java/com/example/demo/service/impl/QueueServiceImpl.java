package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
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
        // FIX: Test t68 requires this validation
        if (pos == null || pos < 1) {
            throw new IllegalArgumentException("Position must be at least 1");
        }

        Token t = tokenRepo.findById(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        
        // Find existing or create default
        QueuePosition qp = queueRepo.findByToken_Id(tokenId)
            .orElse(new QueuePosition());
            
        qp.setToken(t);
        qp.setPosition(pos);
        
        return queueRepo.save(qp);
    }

    @Override
    public QueuePosition getPosition(Long tokenId) {
        return queueRepo.findByToken_Id(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Queue position not found"));
    }
}