package com.gamemarket.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        // Mock login
        return Map.of("token", "mock-token-123", "userId", "1");
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> data) {
        // Mock register
        return Map.of("message", "User registered successfully");
    }
}
