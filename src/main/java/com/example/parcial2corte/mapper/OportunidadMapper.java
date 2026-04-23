package com.example.parcial2corte.mapper;

import com.example.parcial2corte.domain.Cliente;
import com.example.parcial2corte.domain.Oportunidad;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.OportunidadRequest;
import com.example.parcial2corte.dto.OportunidadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OportunidadMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "vendedor", source = "vendedor")
    Oportunidad toEntity(OportunidadRequest req, Cliente cliente, Usuario vendedor);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "vendedorId", source = "vendedor.id")
    OportunidadResponse toResponse(Oportunidad oportunidad);
}
