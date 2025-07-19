package com.challange.service;

import com.challange.entity.EstadoCarritoEntity;
import com.challange.repository.EstadoCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CarritoEstadoService {

    @Autowired
    private EstadoCarritoRepository dao;

    @Cacheable(cacheNames = "estadoCarritoCache", key = "#id")
    public EstadoCarritoEntity findById(Long id){
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró EstadoCarrito con id " + id));
    }
}
