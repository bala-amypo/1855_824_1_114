package com.example.demo.controller;


import com.example.demo.entity.ServiceCounter;
import com.example.demo.service.ServiceCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/counters")
@Tag(name = "Service Counters")
public class ServiceCounterController {


private final ServiceCounterService counterService;


public ServiceCounterController(ServiceCounterService counterService) {
this.counterService = counterService;
}


@PostMapping
@Operation(summary = "Add new service counter")
public ServiceCounter addCounter(@RequestBody ServiceCounter counter) {
return counterService.addCounter(counter);
}


@GetMapping("/active")
@Operation(summary = "Get all active counters")
public List<ServiceCounter> getActiveCounters() {
return counterService.getActiveCounters();
}
}