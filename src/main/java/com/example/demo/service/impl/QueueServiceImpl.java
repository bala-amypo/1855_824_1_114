package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl {
    private final QueuePositionRepository queueRepo;
    private final TokenRepository tokenRepo;

    public void updateQueuePosition(Long tokenId, Integer pos) {
        Token t = tokenRepo.findById(tokenId).orElseThrow();
        QueuePosition qp = new QueuePosition();
        qp.setToken(t);
        qp.setPosition(pos);
        queueRepo.save(qp);
    }
}