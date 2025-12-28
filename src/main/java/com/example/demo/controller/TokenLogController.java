package com.example.demo.controller;
import com.example.demo.entity.TokenLog;
import com.example.demo.service.TokenLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class TokenLogController {
    private final TokenLogService service;
    @PostMapping("/{tokenId}")
    public TokenLog addLog(@PathVariable Long tokenId, @RequestBody String msg) { return service.addLog(tokenId, msg); }
    @GetMapping("/{tokenId}")
    public List<TokenLog> getLogs(@PathVariable Long tokenId) { return service.getLogs(tokenId); }
}