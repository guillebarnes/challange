package com.challange.mapper;

import com.challange.dto.responses.CategoriaDTO;
import com.challange.entity.CategoriaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    public CategoriaDTO entityToDto(CategoriaEntity entity);
}
