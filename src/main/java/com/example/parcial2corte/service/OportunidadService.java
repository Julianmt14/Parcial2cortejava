package com.example.parcial2corte.service;

import com.example.parcial2corte.domain.Cliente;
import com.example.parcial2corte.domain.Oportunidad;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.OportunidadRequest;
import com.example.parcial2corte.dto.OportunidadResponse;
import com.example.parcial2corte.exception.AccesoDenegadoException;
import com.example.parcial2corte.exception.RecursoNoEncontradoException;
import com.example.parcial2corte.mapper.OportunidadMapper;
import com.example.parcial2corte.repository.ClienteRepository;
import com.example.parcial2corte.repository.OportunidadRepository;
import com.example.parcial2corte.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OportunidadService {

    private final ClienteRepository clienteRepository;
    private final OportunidadRepository oportunidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final OportunidadMapper oportunidadMapper;

    @Transactional
    public OportunidadResponse crearParaCliente(Long clienteId, OportunidadRequest req, String username) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + clienteId + " no existe"));
        Usuario vendedor = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario no autenticado"));
        Oportunidad o = oportunidadMapper.toEntity(req, cliente, vendedor);
        return oportunidadMapper.toResponse(oportunidadRepository.save(o));
    }
}
