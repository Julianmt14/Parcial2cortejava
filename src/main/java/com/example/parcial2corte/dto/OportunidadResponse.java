package com.example.parcial2corte.dto;

import com.example.parcial2corte.domain.EstadoOportunidad;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OportunidadResponse(
        Long id,
        String titulo,
        String descripcion,
        BigDecimal monto,
        EstadoOportunidad estado,
        LocalDate fechaCierre,
        Long clienteId,
        Long vendedorId
) {}
