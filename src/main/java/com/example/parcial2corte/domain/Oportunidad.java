package com.example.parcial2corte.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// Oportunidad de venta asociada a un Cliente y gestionada por un Usuario (vendedor).
@Entity
@Table(name = "oportunidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    // BigDecimal para dinero (nunca double/float: pierden precisión).
    // precision=14, scale=2 -> hasta 12 dígitos enteros y 2 decimales.
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOportunidad estado;

    private LocalDate fechaCierre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;
}
