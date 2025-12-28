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
    private final QueuePositionRepository queueRepository;
    private final TokenRepository tokenRepository;

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer newPosition) {
        // FIX: Test t68 requires this validation
        if (newPosition < 1) {
             throw new IllegalArgumentException("Position must be at least 1");
        }
        
        Token t = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
            
        // Find existing or create new (Fixes compilation logic)
        QueuePosition qp = queueRepository.findByToken_Id(tokenId)
                .orElse(new QueuePosition());
        
        qp.setToken(t);
        qp.setPosition(newPosition);
        
        // FIX: Must return the saved object
        return queueRepository.save(qp);
    }

    @Override
    public QueuePosition getPosition(Long tokenId) {
        return queueRepository.findByToken_Id(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue position not found"));
    }
}