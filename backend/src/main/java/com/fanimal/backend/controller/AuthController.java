package com.fanimal.backend.controller;

import com.fanimal.backend.dto.JwtResponse;
import com.fanimal.backend.dto.LoginRequest;
import com.fanimal.backend.dto.RegisterRequest;
import com.fanimal.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            JwtResponse jwtResponse = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<JwtResponse> logout() {
        try {
            return ResponseEntity.status(HttpStatus.OK).build();
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//| Method | Endpoint           | Description               |
//| ------ | ------------------ | ------------------------- |
//| POST   | /api/auth/register | Register new user         |
//| POST   | /api/auth/login    | Login, returns JWT        |
//| POST   | /api/auth/logout   | Logout / invalidate token |
}
