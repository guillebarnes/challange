package com.challange.mapper;

import com.challange.dto.responses.ClienteDTO;
import com.challange.entity.ClienteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    public ClienteDTO entityToDto(ClienteEntity entity);
}
