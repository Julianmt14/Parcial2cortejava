package com.example.parcial2corte.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Entidad Cliente — agregado central del CRM. Tiene un vendedor (ManyToOne) y
// colecciones de contactos y oportunidades (OneToMany).
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(nullable = false)
    private String email;

    private String telefono;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    // Lado DUEÑO de la relación: este lado lleva la FK (columna vendedor_id en BD).
    // LAZY para no cargar el vendedor a menos que se acceda explícitamente.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    // Lado INVERSO 1-N hacia Contacto. orphanRemoval=true elimina contactos
    // que se quitan de la lista (limpieza automática).
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contacto> contactos = new ArrayList<>();

    // Igual que arriba, pero para oportunidades.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Oportunidad> oportunidades = new ArrayList<>();
}
