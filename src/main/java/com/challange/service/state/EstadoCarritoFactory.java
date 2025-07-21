package com.challange.service.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EstadoCarritoFactory {

    @Autowired
    private Map<String, EstadoCarrito> estados;

    public EstadoCarrito obtenerEstado(String descripcion){
        return estados.get(descripcion);
    }
}
