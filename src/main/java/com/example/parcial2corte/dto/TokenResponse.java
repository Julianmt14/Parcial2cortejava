package com.example.parcial2corte.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {}
