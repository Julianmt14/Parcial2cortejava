package com.example.parcial2corte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactoRequest(
        @NotBlank @Size(max = 120) String nombre,
        String cargo,
        @Email String email,
        String telefono
) {}
