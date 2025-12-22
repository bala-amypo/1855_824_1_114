package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "service_counters")
public class ServiceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String counterName;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Boolean isActive;

    // No-args constructor
    public ServiceCounter() {
    }

    // All-args constructor
    public ServiceCounter(Long id, String counterName, String department, Boolean isActive) {
        this.id = id;
        this.counterName = counterName;
        this.department = department;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
