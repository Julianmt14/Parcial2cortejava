package com.example.parcial2corte.dto;

public record ClienteResponse(
        Long id,
        String nombre,
        String nit,
        String email,
        String telefono,
        boolean activo,
        Long vendedorId
) {}
