package com.edulibrary.dashboard.controller;

import com.edulibrary.dashboard.dto.LoginRequest;
import com.edulibrary.dashboard.dto.RegisterRequest;
import com.edulibrary.dashboard.dto.MessageResponse;
import com.edulibrary.dashboard.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Simple POST /api/auth/register endpoint
     * Saves user directly to DB with plain text password
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request.getEmail(), request.getPassword()));
    }

    /**
     * Simple POST /api/auth/login endpoint
     * Returns success if email & password match, else error
     */
    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@RequestBody LoginRequest request) {
        MessageResponse response = authService.login(request.getEmail(), request.getPassword());
        if ("Login successful".equals(response.message())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
