package com.example.demo.controller;
import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.ServiceCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/counters")
@RequiredArgsConstructor
public class ServiceCounterController {
    private final ServiceCounterService service;

    @PostMapping
    public ServiceCounter addCounter(@RequestBody ServiceCounter sc) { return service.addCounter(sc); }

    @GetMapping("/active")
    public List<ServiceCounter> getActive() { return service.getActiveCounters(); }
}