package com.example.Test.controller;

import com.example.Test.dto.Auth.LoginRequest;
import com.example.Test.dto.Auth.RegisterRequest;
import com.example.Test.dto.Auth.AuthResponse;
import com.example.Test.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok().body("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(resp);
    }
}
