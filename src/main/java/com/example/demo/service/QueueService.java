package com.example.demo.service;

import com.example.demo.model.QueuePosition;

public interface QueueService {
    QueuePosition updateQueuePosition(Long tokenId, Integer newPosition);
    Integer getPosition(Long tokenId);
}