package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String tokenNumber;
    private String status;
    
    @ManyToOne
    private ServiceCounter serviceCounter;
    
    // FIX: Initialize immediately. Tests use 'new Token()' which skips @PrePersist.
    private LocalDateTime issuedAt = LocalDateTime.now();
    private LocalDateTime completedAt;

    @PrePersist
    public void onCreate() { 
        if(issuedAt == null) issuedAt = LocalDateTime.now(); 
    }
}