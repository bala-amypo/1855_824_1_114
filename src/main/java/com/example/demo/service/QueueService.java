package com.example.demo.service;
import com.example.demo.entity.QueuePosition;

public interface QueueService {
    // FIX: Changed void to QueuePosition
    QueuePosition updateQueuePosition(Long tokenId, Integer newPosition);
    
    // FIX: Added missing method
    QueuePosition getPosition(Long tokenId);
}