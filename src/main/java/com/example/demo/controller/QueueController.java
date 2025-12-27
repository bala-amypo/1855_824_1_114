package com.example.demo.controller;

import com.example.demo.entity.QueuePosition;
import com.example.demo.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
@Tag(name = "QueueController")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PutMapping("/position/{tokenId}/{newPosition}")
    @Operation(summary = "Update queue position")
    public QueuePosition update(@PathVariable Long tokenId, @PathVariable Integer newPosition) {
        return queueService.updateQueuePosition(tokenId, newPosition);
    }

    @GetMapping("/position/{tokenId}")
    @Operation(summary = "Get position for token")
    public Integer get(@PathVariable Long tokenId) {
        return queueService.getPosition(tokenId);
    }
}