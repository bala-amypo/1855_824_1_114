package com.example.demo.service.impl;

import com.example.demo.entity.QueuePosition;
import com.example.demo.entity.Token;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.QueueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepository;

    public QueueServiceImpl(QueuePositionRepository queueRepo, TokenRepository tokenRepository) {
        this.queueRepo = queueRepo;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public QueuePosition updateQueuePosition(Long tokenId, Integer position) {
        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        QueuePosition qp = queueRepo.findByToken_Id(tokenId).orElse(new QueuePosition());
        qp.setToken(token);
        qp.setPosition(position);
        qp.setUpdatedAt(LocalDateTime.now());
        return queueRepo.save(qp);
    }

    // ✅ REQUIRED by interface
    @Override
    public Integer getPosition(Long tokenId) {
        QueuePosition qp = queueRepo.findByToken_Id(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue position not found"));
        return qp.getPosition();
    }
}