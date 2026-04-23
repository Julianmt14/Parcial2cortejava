package com.example.parcial2corte.mapper;

import com.example.parcial2corte.domain.Cliente;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.ClienteRequest;
import com.example.parcial2corte.dto.ClienteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

// Mapper MapStruct (componentModel = SPRING => se inyecta como bean).
// Centraliza la conversión DTO <-> entidad: el controlador y el servicio quedan limpios.
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "nombre", source = "req.nombre")
    @Mapping(target = "nit", source = "req.nit")
    @Mapping(target = "email", source = "req.email")
    @Mapping(target = "telefono", source = "req.telefono")
    @Mapping(target = "vendedor", source = "vendedor")
    @Mapping(target = "contactos", ignore = true)
    @Mapping(target = "oportunidades", ignore = true)
    Cliente toEntity(ClienteRequest req, Usuario vendedor);

    @Mapping(target = "vendedorId", source = "vendedor.id")
    ClienteResponse toResponse(Cliente cliente);
}
