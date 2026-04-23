package com.example.parcial2corte.dto;

import com.example.parcial2corte.domain.EstadoOportunidad;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OportunidadRequest(
        @NotBlank @Size(max = 150) String titulo,
        @Size(max = 500) String descripcion,
        @NotNull @DecimalMin("0.00") BigDecimal monto,
        @NotNull EstadoOportunidad estado,
        @Future LocalDate fechaCierre
) {}
