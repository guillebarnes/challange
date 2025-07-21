package com.challange.service.state;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.entity.CarritoEntity;

public interface EstadoCarrito {
    CarritoEntity agregarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO productoSeleccionado);
    void finalizar(Long idCarrito);
    CarritoEntity eliminarUnidadDeProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto);
    CarritoEntity eliminarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto);
}
