package com.challange.mapper;

import com.challange.dto.responses.CarritoProductoDTO;
import com.challange.entity.CarritoProductoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarritoProductoMapper {
    public CarritoProductoDTO entityToDto(CarritoProductoEntity entity);
    public List<CarritoProductoDTO> entitiesToDtos(List<CarritoProductoEntity> entites);
}
