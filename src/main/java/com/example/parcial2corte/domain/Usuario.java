package com.example.parcial2corte.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Entidad JPA del usuario del sistema. Contiene credenciales y rol para Spring Security.
// @Entity la marca como entidad gestionada por JPA; @Table fija el nombre real de la tabla
// y declara que "username" es UNIQUE a nivel de base de datos.
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    // Llave primaria autogenerada por la BD (IDENTITY).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String username;

    // Se guarda HASHEADO (BCrypt) — nunca en texto plano.
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    // Enum guardado como texto ('ADMIN','VENDEDOR','LECTOR') en lugar de ordinal,
    // así si cambia el orden del enum no se rompe la BD.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    // Lado INVERSO de la relación 1-N con Cliente.
    // mappedBy="vendedor" indica que la FK la lleva la otra entidad (Cliente.vendedor),
    // así JPA NO crea una segunda columna ni tabla puente.
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cliente> clientes = new ArrayList<>();
}
