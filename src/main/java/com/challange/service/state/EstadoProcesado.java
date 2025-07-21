package com.challange.service.state;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.entity.CarritoEntity;
import org.springframework.stereotype.Component;

@Component("PROCESADO")
public class EstadoProcesado implements EstadoCarrito{

    @Override
    public CarritoEntity agregarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO productoSeleccionado){
        throw new RuntimeException("No se puede agregar productos a un carrito procesado");
    }

    @Override
    public void finalizar(Long idCarrito){
        throw new RuntimeException("No se puede finalizar un carrito ya procesado");
    }

    @Override
    public CarritoEntity eliminarUnidadDeProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto){
        throw new RuntimeException("No se puede borrar productos de un carrito procesado");
    }

    @Override
    public CarritoEntity eliminarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto){
        throw new RuntimeException("No se puede borrar productos de un carrito procesado");
    }
}
