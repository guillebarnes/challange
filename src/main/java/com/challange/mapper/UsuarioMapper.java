package com.challange.mapper;

import com.challange.dto.requests.UsuarioLoginDTO;
import com.challange.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    public UsuarioLoginDTO entityToDto(UsuarioEntity entity);
    public UsuarioEntity dtoToEntity(UsuarioLoginDTO dto);
}
