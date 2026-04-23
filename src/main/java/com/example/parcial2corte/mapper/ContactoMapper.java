package com.example.parcial2corte.mapper;

import com.example.parcial2corte.domain.Contacto;
import com.example.parcial2corte.dto.ContactoRequest;
import com.example.parcial2corte.dto.ContactoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Contacto toEntity(ContactoRequest req);

    @Mapping(target = "clienteId", source = "cliente.id")
    ContactoResponse toResponse(Contacto contacto);
}
