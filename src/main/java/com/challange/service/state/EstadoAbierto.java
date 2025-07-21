package com.challange.service.state;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.entity.CarritoEntity;
import com.challange.entity.CarritoProductoEntity;
import com.challange.entity.ProductoEntity;
import com.challange.entity.id.CarritoProductoId;
import com.challange.repository.CarritoProductoRepository;
import com.challange.service.CarritoEstadoService;
import com.challange.service.CarritoService;
import com.challange.service.ProcesamientoService;
import com.challange.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component("ABIERTO")
public class EstadoAbierto implements EstadoCarrito {
    private static final Long ESTADO_VACIO = 3L;

    @Autowired
    private CarritoProductoRepository carritoProductoDao;

    @Autowired
    private ProductoService productoSvc;

    @Autowired
    private ProcesamientoService procesamientoSvc;

    @Autowired
    private CarritoService carritoSvc;

    @Autowired
    private CarritoEstadoService carritoEstadoSvc;

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

        return carrito;
    }

    @Override
    public void finalizar(Long idCarrito) {
        Thread procesoAsincrono = new Thread(() ->
                procesamientoSvc.finalizarCarrito(idCarrito));
        procesoAsincrono.start();
    }

    @Override
    public CarritoEntity eliminarUnidadDeProducto(CarritoEntity carritoEntity, ProductoSeleccionadoDTO productoSeleccionadoDTO){
        CarritoProductoId id = new CarritoProductoId(carritoEntity.getCarritoId(), productoSeleccionadoDTO.getIdProducto());
        Long carritoId = carritoEntity.getCarritoId();
        CarritoProductoEntity carritoProductoEntity = carritoProductoDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró CarritoProducto con id " + carritoId + "-" + productoSeleccionadoDTO.getIdProducto()));

        //Resto 1 unidad a la cantidad
        Integer cantidad = carritoProductoEntity.getCantidad();

        if (cantidad > 1) {
            Double nuevoSubtotal = carritoSvc.calcularNuevoSubtotal(cantidad, carritoProductoEntity.getSubtotal());
            carritoProductoEntity.setCantidad(cantidad - 1);
            carritoProductoEntity.setSubtotal(nuevoSubtotal);
            carritoProductoDao.save(carritoProductoEntity);
        } else {
            carritoSvc.eliminarProductoDelCarrito(carritoEntity.getCarritoId(), productoSeleccionadoDTO);
            if(carritoEntity.getCarritoProductos().isEmpty()){
                carritoEntity.setEstado(carritoEstadoSvc.findById(ESTADO_VACIO));
                carritoEntity = carritoSvc.guardarCarrito(carritoEntity);
            }
        }

        return carritoEntity;
    }

    @Override
    public CarritoEntity eliminarProducto(CarritoEntity carritoEntity, ProductoSeleccionadoDTO productoSeleccionadoDTO){
        CarritoProductoId id = new CarritoProductoId(carritoEntity.getCarritoId(), productoSeleccionadoDTO.getIdProducto());
        try {
            carritoProductoDao.deleteById(id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EntityNotFoundException("No se encontró el producto " + productoSeleccionadoDTO.getIdProducto() +" para borrar del carrito " + carritoEntity.getCarritoId());
        }

        if(carritoEntity.getCarritoProductos().isEmpty()){
            carritoEntity.setEstado(carritoEstadoSvc.findById(ESTADO_VACIO));
            carritoEntity = carritoSvc.guardarCarrito(carritoEntity);
        }

        return carritoEntity;
    }
}
