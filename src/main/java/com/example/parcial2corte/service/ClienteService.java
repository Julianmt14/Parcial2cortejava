package com.example.parcial2corte.service;

import com.example.parcial2corte.dto.ClienteRequest;
import com.example.parcial2corte.dto.ClienteResponse;

import java.util.List;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest req);
    ClienteResponse buscarPorId(Long id);
    List<ClienteResponse> listar();
    ClienteResponse actualizar(Long id, ClienteRequest req);
    void eliminar(Long id, String usernameSolicitante);
}
