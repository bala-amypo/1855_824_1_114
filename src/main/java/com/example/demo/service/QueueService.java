package com.example.demo.service;
import com.example.demo.entity.QueuePosition;

public interface QueueService {
    // Change void to QueuePosition
    QueuePosition updateQueuePosition(Long tokenId, Integer newPosition);
}