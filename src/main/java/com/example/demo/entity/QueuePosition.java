package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_positions")
@Data
public class QueuePosition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private Token token;
    
    private Integer position;
    
    // Fixed: Initialize immediately for tests
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PrePersist @PreUpdate
    public void onUpdate() { updatedAt = LocalDateTime.now(); }
}