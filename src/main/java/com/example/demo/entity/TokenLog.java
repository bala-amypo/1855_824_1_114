package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_logs")
@Data
public class TokenLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Token token;
    
    private String logMessage;
    
    // FIX: Initialize immediately (Fixes t30)
    private LocalDateTime loggedAt = LocalDateTime.now();
    
    @PrePersist
    public void onCreate() { if(loggedAt == null) loggedAt = LocalDateTime.now(); }
}