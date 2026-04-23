package com.example.parcial2corte.mapper;

import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {
    UsuarioResponse toResponse(Usuario usuario);
}
