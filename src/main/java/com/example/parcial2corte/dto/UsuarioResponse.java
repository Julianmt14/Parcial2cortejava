package com.example.parcial2corte.dto;

import com.example.parcial2corte.domain.Rol;

public record UsuarioResponse(
        Long id,
        String username,
        String email,
        Rol rol
) {}
