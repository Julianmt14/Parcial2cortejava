package com.example.parcial2corte.controller;

import com.example.parcial2corte.dto.LoginRequest;
import com.example.parcial2corte.dto.SignupRequest;
import com.example.parcial2corte.dto.TokenResponse;
import com.example.parcial2corte.dto.UsuarioResponse;
import com.example.parcial2corte.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UsuarioResponse> signup(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
