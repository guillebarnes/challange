package com.challange.mapper;

import com.challange.dto.responses.ProductoDTO;
import com.challange.entity.ProductoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    public ProductoDTO entityToDto(ProductoEntity entity);
}
