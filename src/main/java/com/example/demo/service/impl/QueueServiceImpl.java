package com.example.demo.service.impl;

import com.example.demo.entity.*;
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
    public void updateQueuePosition(Long tokenId, Integer newPosition) {
        Token t = tokenRepository.findById(tokenId).orElseThrow();
        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(newPosition);
        queueRepository.save(qp);
    }
}