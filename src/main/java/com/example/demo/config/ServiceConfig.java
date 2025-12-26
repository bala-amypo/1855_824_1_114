package com.example.demo.config;

import com.example.demo.repository.QueuePositionRepository;
import com.example.demo.repository.ServiceCounterRepository;
import com.example.demo.repository.TokenLogRepository;
import com.example.demo.repository.TokenRepository;
import com.example.demo.service.TokenService;
import com.example.demo.service.impl.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public TokenService tokenService(
            TokenRepository tokenRepository,
            ServiceCounterRepository serviceCounterRepository,
            TokenLogRepository tokenLogRepository,
            QueuePositionRepository queuePositionRepository
    ) {
        return new TokenServiceImpl(
                tokenRepository,
                serviceCounterRepository,
                tokenLogRepository,
                queuePositionRepository
        );
    }
}