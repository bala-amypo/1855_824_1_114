package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.QueueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueuePositionRepository queuePositionRepository;
    private final TokenRepository tokenRepository;

    // QueueService: (QueuePositionRepository, TokenRepository)
    public QueueServiceImpl(QueuePositionRepository queuePositionRepository,
                            TokenRepository tokenRepository) {
        this.queuePositionRepository = queuePositionRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer newPosition) {
        if (newPosition == null || newPosition < 1) {
            throw new IllegalArgumentException("Position must be >= 1");
        }

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        QueuePosition qp = queuePositionRepository.findByToken_Id(tokenId);
        if (qp == null) {
            qp = new QueuePosition();
            qp.setToken(token);
        }
        qp.setPosition(newPosition);
        qp.setUpdatedAt(LocalDateTime.now());
        return queuePositionRepository.save(qp);
    }

    @Override
    public Integer getPosition(Long tokenId) {
        QueuePosition qp = queuePositionRepository.findByToken_Id(tokenId);
        if (qp == null) {
            throw new ResourceNotFoundException("Queue position not found");
        }
        return qp.getPosition();
    }
}