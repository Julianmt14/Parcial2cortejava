package com.example.parcial2corte.service;

import com.example.parcial2corte.domain.Cliente;
import com.example.parcial2corte.domain.Contacto;
import com.example.parcial2corte.dto.ContactoRequest;
import com.example.parcial2corte.dto.ContactoResponse;
import com.example.parcial2corte.exception.RecursoNoEncontradoException;
import com.example.parcial2corte.mapper.ContactoMapper;
import com.example.parcial2corte.repository.ClienteRepository;
import com.example.parcial2corte.repository.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final ClienteRepository clienteRepository;
    private final ContactoRepository contactoRepository;
    private final ContactoMapper contactoMapper;

    @Transactional
    public ContactoResponse asociarACliente(Long clienteId, ContactoRequest req) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente " + clienteId + " no existe"));
        Contacto contacto = contactoMapper.toEntity(req);
        contacto.setCliente(cliente);
        return contactoMapper.toResponse(contactoRepository.save(contacto));
    }
}
