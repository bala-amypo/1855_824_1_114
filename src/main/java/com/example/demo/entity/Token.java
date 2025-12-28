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
    private String status; // WAITING, SERVING, COMPLETED
    
    @ManyToOne
    private ServiceCounter serviceCounter;
    
    private LocalDateTime issuedAt;
    private LocalDateTime completedAt;

    @PrePersist
    public void onCreate() {
        if (issuedAt == null) issuedAt = LocalDateTime.now();
    }
}