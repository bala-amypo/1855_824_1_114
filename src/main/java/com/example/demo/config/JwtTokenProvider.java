package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secret;
    private final long expiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, 
                            @Value("${jwt.expiration}") long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    private Key getKey() { return Keys.hmacShaKeyFor(secret.getBytes()); }

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                // FIX: Test t54 requires the Subject to be the User ID
                .setSubject(String.valueOf(userId))
                // FIX: Test t46 requires these specific claims
                .claim("email", email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) { return false; }
    }
    
    public io.jsonwebtoken.Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }
}