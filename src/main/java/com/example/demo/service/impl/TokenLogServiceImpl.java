package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenLogServiceImpl {
    private final TokenLogRepository logRepo;
    private final TokenRepository tokenRepo;

    public TokenLog addLog(Long tokenId, String msg) {
        Token t = tokenRepo.findById(tokenId).orElseThrow();
        TokenLog log = new TokenLog();
        log.setToken(t);
        log.setLogMessage(msg);
        return logRepo.save(log);
    }

    public List<TokenLog> getLogs(Long tokenId) {
        return logRepo.findByToken_IdOrderByLoggedAtAsc(tokenId);
    }
}