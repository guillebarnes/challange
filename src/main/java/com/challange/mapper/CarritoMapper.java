package com.challange.mapper;

import com.challange.dto.responses.CarritoDTO;
import com.challange.entity.CarritoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoMapper {
    CarritoDTO entityToDto(CarritoEntity entity);

    List<CarritoDTO> entitiesToDtos(List<CarritoEntity> entities);
}
