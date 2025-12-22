package com.example.demo.controller;

import com.example.demo.model.ServiceCounter;
import com.example.demo.service.ServiceCounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counters")
public class ServiceCounterController {

    private final ServiceCounterService serviceCounterService;

    public ServiceCounterController(ServiceCounterService serviceCounterService) {
        this.serviceCounterService = serviceCounterService;
    }


    @PostMapping
    public ResponseEntity<ServiceCounter> addCounter(@RequestBody ServiceCounter counter) {
        return new ResponseEntity<>(
                serviceCounterService.addCounter(counter),
                HttpStatus.CREATED
        );
    }

    
    @GetMapping("/active")
    public ResponseEntity<List<ServiceCounter>> getActiveCounters() {
        return ResponseEntity.ok(serviceCounterService.getActiveCounters());
    }
}
