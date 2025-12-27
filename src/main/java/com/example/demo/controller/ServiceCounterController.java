// package com.example.demo.controller;

// import com.example.demo.entity.ServiceCounter;
// import com.example.demo.service.ServiceCounterService;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/counters")
// @Tag(name = "ServiceCounterController")
// public class ServiceCounterController {

//     private final ServiceCounterService service;

//     public ServiceCounterController(ServiceCounterService service) {
//         this.service = service;
//     }

//     @PostMapping("/")
//     @Operation(summary = "Add counter")
//     public ServiceCounter addCounter(@RequestBody ServiceCounter counter) {
//         return service.addCounter(counter);
//     }

//     @GetMapping("/active")
//     @Operation(summary = "List active counters")
//     public List<ServiceCounter> getActiveCounters() {
//         return service.getActiveCounters();
//     }
// }