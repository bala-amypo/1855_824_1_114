package com.example.demo.controller;
import com.example.demo.entity.Token;
import com.example.demo.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService service;

    @PostMapping("/issue")
    public Token issue(@RequestParam Long counterId) { return service.issueToken(counterId); }

    @PutMapping("/{id}/status")
    public Token updateStatus(@PathVariable Long id, @RequestParam String status) { return service.updateStatus(id, status); }
    
    @GetMapping("/{id}")
    public Token getToken(@PathVariable Long id) { return service.getToken(id); }
}