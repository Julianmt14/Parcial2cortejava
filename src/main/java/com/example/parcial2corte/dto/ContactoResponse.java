package com.example.parcial2corte.dto;

public record ContactoResponse(
        Long id,
        String nombre,
        String cargo,
        String email,
        String telefono,
        Long clienteId
) {}
