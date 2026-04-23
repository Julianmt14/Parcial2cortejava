package com.example.parcial2corte.domain;

import jakarta.persistence.*;
import lombok.*;

// Contacto de un cliente (persona dentro de la empresa cliente: gerente, comprador, etc.).
@Entity
@Table(name = "contactos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    private String cargo;
    private String email;
    private String telefono;

    // Lado DUEÑO: la columna cliente_id está en la tabla contactos.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
