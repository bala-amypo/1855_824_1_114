package com.example.demo.service;
import com.example.demo.entity.QueuePosition;

public interface QueueService {
    // FIX: Must return QueuePosition, not void
    QueuePosition updateQueuePosition(Long tokenId, Integer newPosition);
    
    // FIX: Added missing method
    QueuePosition getPosition(Long tokenId);
}