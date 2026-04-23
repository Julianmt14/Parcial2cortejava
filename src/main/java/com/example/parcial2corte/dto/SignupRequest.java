package com.example.parcial2corte.dto;

import com.example.parcial2corte.domain.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 4, max = 60) String username,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Email String email,
        Rol rol
) {}
