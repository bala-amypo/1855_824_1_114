package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException; // Ensure this is imported
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
        if (newPosition < 1) {
             throw new IllegalArgumentException("Position must be at least 1");
        }
        
        Token t = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
            
        // Find existing position or create new one
        QueuePosition qp = queueRepository.findByToken_Id(tokenId)
                .orElse(new QueuePosition());
        
        qp.setToken(t);
        qp.setPosition(newPosition);
        
        // FIX: Must return the saved object, not void
        return queueRepository.save(qp);
    }

    // FIX: Added missing method required by tests
    public QueuePosition getPosition(Long tokenId) {
        return queueRepository.findByToken_Id(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue position not found"));
    }
}