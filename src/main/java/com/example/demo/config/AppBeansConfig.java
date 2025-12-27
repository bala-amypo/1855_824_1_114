package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeansConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        // Must be >= 32 chars for HS256
        return new JwtTokenProvider("ChangeThisSecretKeyReplaceMe1234567890", 3600000);
    }
}