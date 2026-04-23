package com.example.parcial2corte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 20) String nit,
        @NotBlank @Email String email,
        String telefono,
        @NotNull Long vendedorId
) {}
