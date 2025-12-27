package com.example.demo.controller;

import com.example.demo.entity.QueuePosition;
import com.example.demo.service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    // Example: GET /api/queue/position/5
    @GetMapping("/position/{tokenId}")
    public ResponseEntity<QueuePosition> getPosition(@PathVariable Long tokenId) {
        QueuePosition position = queueService.getPosition(tokenId);   // ✅ QueuePosition
        return ResponseEntity.ok(position);
    }
}