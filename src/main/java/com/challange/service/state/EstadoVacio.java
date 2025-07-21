package com.challange.service.state;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.entity.CarritoEntity;
import com.challange.entity.CarritoProductoEntity;
import com.challange.entity.ProductoEntity;
import com.challange.entity.id.CarritoProductoId;
import com.challange.repository.CarritoProductoRepository;
import com.challange.service.CarritoService;
import com.challange.service.ProcesamientoService;
import com.challange.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("VACIO")
public class EstadoVacio implements EstadoCarrito {
    private static final Long ESTADO_ABIERTO = 1L;
    private static final Long ESTADO_VACIO = 3L;

    @Autowired
    private CarritoProductoRepository carritoProductoDao;

    @Autowired
    private ProductoService productoSvc;

    @Autowired
    private ProcesamientoService procesamientoSvc;

    @Autowired
    private CarritoService carritoSvc;

    @Override
    public CarritoEntity agregarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO productoSeleccionado){
        CarritoProductoId id = new CarritoProductoId(carrito.getCarritoId(), productoSeleccionado.getIdProducto());

        CarritoProductoEntity carritoProductoEntity = new CarritoProductoEntity();
        ProductoEntity productoEntity = productoSvc.findById(productoSeleccionado.getIdProducto());

        carritoProductoEntity.setId(id);
        carritoProductoEntity.setCarrito(carrito);
        carritoProductoEntity.setProducto(productoEntity);
        carritoProductoEntity.setCantidad(productoSeleccionado.getCantidad());
        carritoProductoEntity.setEnPromocion(productoEntity.isEnPromocion());
        carritoProductoEntity.setSubtotal(productoEntity.getPrecio() * productoSeleccionado.getCantidad());

        carritoProductoDao.save(carritoProductoEntity);
        //Modifico el estado del carrito que ahora al tener un producto pasa a estar abierto
        if (!carrito.getCarritoProductos().isEmpty())
            carrito = carritoSvc.actualizarEstadoDelCarrito(carrito, ESTADO_ABIERTO);

        return carrito;
    }

    @Override
    public void finalizar(Long idCarrito) {
        throw new RuntimeException("No se puede finalizar un carrito vacio");
    }

    @Override
    public CarritoEntity eliminarUnidadDeProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto){
        throw new RuntimeException("No se puede borrar productos de un carrito vacio");
    }

    @Override
    public CarritoEntity eliminarProducto(CarritoEntity carrito, ProductoSeleccionadoDTO producto){
        throw new RuntimeException("No se puede borrar productos de un carrito vacio");
    }

}
