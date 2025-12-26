package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    // Very simple token generator (no external libs)
    public String generateToken(Long userId, String email, String role) {
        // just return a predictable string token
        return "token-" + userId + "-" + (email == null ? "na" : email) + "-" + (role == null ? "STAFF" : role);
    }
}