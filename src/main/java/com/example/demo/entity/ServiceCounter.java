package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "service_counters")
@Data
public class ServiceCounter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String counterName;
    private String department;
    private Boolean isActive = true;
}