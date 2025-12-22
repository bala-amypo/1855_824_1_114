// package com.example.demo.config;


// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import org.springframework.stereotype.Component;


// import java.util.Date;


// @Component
// public class JwtTokenProvider {


// private final String JWT_SECRET = "secret-key-demo";
// private final long JWT_EXPIRATION = 86400000; // 1 day


// public String generateToken(Long userId, String email, String role) {
// return Jwts.builder()
// .setSubject(email)
// .claim("userId", userId)
// .claim("role", role)
// .setIssuedAt(new Date())
// .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
// .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
// .compact();
// }


// public Claims getClaims(String token) {
// return Jwts.parser()
// .setSigningKey(JWT_SECRET)
// .parseClaimsJws(token)
// .getBody();
// }


// public String getEmail(String token) {
// return getClaims(token).getSubject();
// }
// }