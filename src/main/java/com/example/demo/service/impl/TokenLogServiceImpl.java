package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenLogServiceImpl implements TokenLogService {
    private final TokenLogRepository logRepository;
    private final TokenRepository tokenRepository;

    @Override
    public TokenLog addLog(Long tokenId, String message) {
        Token t = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        TokenLog log = new TokenLog();
        log.setToken(t);
        log.setLogMessage(message);
        return logRepository.save(log);
    }

    @Override
    public List<TokenLog> getLogs(Long tokenId) {
        return logRepository.findByToken_IdOrderByLoggedAtAsc(tokenId);
    }
}