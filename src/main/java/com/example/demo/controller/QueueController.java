package com.example.demo.controller;
import com.example.demo.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService service;
    @PutMapping("/{tokenId}")
    public void updatePosition(@PathVariable Long tokenId, @RequestParam Integer pos) { service.updateQueuePosition(tokenId, pos); }
}