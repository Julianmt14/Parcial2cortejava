package com.example.parcial2corte.repository;

import com.example.parcial2corte.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByNit(String nit);
    Optional<Cliente> findByNit(String nit);
    List<Cliente> findByVendedor_Id(Long vendedorId);
}
