package com.example.parcial2corte.repository;

import com.example.parcial2corte.domain.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OportunidadRepository extends JpaRepository<Oportunidad, Long> {
    List<Oportunidad> findByCliente_Id(Long clienteId);
}
