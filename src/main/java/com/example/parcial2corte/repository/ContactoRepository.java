package com.example.parcial2corte.repository;

import com.example.parcial2corte.domain.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    List<Contacto> findByCliente_Id(Long clienteId);
}
