package com.example.parcial2corte.service;

import com.example.parcial2corte.domain.Cliente;
import com.example.parcial2corte.domain.Rol;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.ClienteRequest;
import com.example.parcial2corte.dto.ClienteResponse;
import com.example.parcial2corte.exception.AccesoDenegadoException;
import com.example.parcial2corte.exception.RecursoNoEncontradoException;
import com.example.parcial2corte.exception.ReglaNegocioException;
import com.example.parcial2corte.mapper.ClienteMapper;
import com.example.parcial2corte.repository.ClienteRepository;
import com.example.parcial2corte.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementación del servicio de clientes. Concentra TODA la lógica de negocio
// (validaciones, reglas de autorización de dominio, transacciones).
// El controlador no debe tener nada de esto.
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;

    // Crea un cliente. Valida unicidad de NIT y existencia del vendedor antes de persistir.
    @Override
    @Transactional
    public ClienteResponse crear(ClienteRequest req) {
        // Regla de negocio: NIT único.
        if (clienteRepository.existsByNit(req.nit())) {
            throw new ReglaNegocioException("El NIT '" + req.nit() + "' ya esta registrado");
        }
        // Regla de integridad: el vendedor referenciado debe existir.
        Usuario vendedor = usuarioRepository.findById(req.vendedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Vendedor " + req.vendedorId() + " no existe"));
        // Conversión DTO -> entidad delegada al mapper (NO se hace manual aquí).
        Cliente cliente = clienteMapper.toEntity(req, vendedor);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + id + " no existe"));
        return clienteMapper.toResponse(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest req) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + id + " no existe"));
        if (!c.getNit().equals(req.nit()) && clienteRepository.existsByNit(req.nit())) {
            throw new ReglaNegocioException("El NIT '" + req.nit() + "' ya esta registrado");
        }
        Usuario vendedor = usuarioRepository.findById(req.vendedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Vendedor " + req.vendedorId() + " no existe"));
        c.setNombre(req.nombre());
        c.setNit(req.nit());
        c.setEmail(req.email());
        c.setTelefono(req.telefono());
        c.setVendedor(vendedor);
        return clienteMapper.toResponse(c);
    }

    // REGLA OBLIGATORIA del parcial: solo un usuario con rol ADMIN puede eliminar.
    // Esto se valida también con @PreAuthorize en el controlador (defensa en profundidad).
    @Override
    @Transactional
    public void eliminar(Long id, String usernameSolicitante) {
        Usuario solicitante = usuarioRepository.findByUsername(usernameSolicitante)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario no autenticado"));
        if (solicitante.getRol() != Rol.ADMIN) {
            throw new AccesoDenegadoException("Solo ADMIN puede eliminar clientes");
        }
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + id + " no existe"));
        clienteRepository.delete(c);
    }
}
