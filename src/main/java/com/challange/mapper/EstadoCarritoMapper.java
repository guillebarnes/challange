package com.challange.mapper;

import com.challange.dto.responses.EstadoCarritoDTO;
import com.challange.entity.EstadoCarritoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoCarritoMapper {
    public EstadoCarritoDTO entityToDto(EstadoCarritoEntity entity);
}
