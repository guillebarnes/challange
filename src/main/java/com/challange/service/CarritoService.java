package com.challange.service;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.dto.responses.CarritoDTO;
import com.challange.dto.responses.CarritoProductoDTO;
import com.challange.dto.responses.MensajeDTO;
import com.challange.entity.CarritoEntity;
import com.challange.entity.CarritoProductoEntity;
import com.challange.entity.ProductoEntity;
import com.challange.entity.id.CarritoProductoId;
import com.challange.mapper.CarritoMapper;
import com.challange.mapper.CarritoProductoMapper;
import com.challange.repository.CarritoProductoRepository;
import com.challange.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CarritoService {

    private static final Long ESTADO_ABIERTO = 1L;
    private static final Long ESTADO_VACIO = 3L;
    @Autowired
    private CarritoRepository carritoDao;
    @Autowired
    private CarritoProductoRepository carritoProductoDao;
    @Autowired
    private CarritoEstadoService carritoEstadoSvc;
    @Autowired
    private ClienteService usuarioSvc;
    @Autowired
    private ProductoService productoSvc;
    @Autowired
    private ProcesamientoService procesamientoSvc;

    @Autowired
    private CarritoMapper mapper;
    @Autowired
    private CarritoProductoMapper carritoProductoMapper;
    public CarritoDTO crearCarrito(Long idCliente) {
        CarritoEntity entity = new CarritoEntity();
        entity.setCliente(usuarioSvc.findById(idCliente));
        entity.setEstado(carritoEstadoSvc.findById(ESTADO_VACIO));
        CarritoEntity carritoEntity =  carritoDao.save(entity);
        return mapper.entityToDto(carritoEntity);
    }

    public CarritoEntity findById(Long idCarrito){
        return carritoDao.findById(idCarrito)
                .orElseThrow(() -> new EntityNotFoundException("No existe carrito asociado al id: " + idCarrito));
    }

    public List<CarritoDTO> obtenerCarritosPorCliente(Long idCliente){
        List<CarritoEntity> entities = carritoDao.obtenerCarritosPorCliente(idCliente);
        return mapper.entitiesToDtos(entities);
    }
    public CarritoDTO agregarProductoACarrito(Long idCarrito, ProductoSeleccionadoDTO productoSeleccionado){
        CarritoEntity carritoEntity = this.findById(idCarrito);
        if(carritoEntity.getEstado().getDescripcion().equals("PROCESADO"))
            throw new RuntimeException("No se puede agregar productos a un carrito procesado");

        //Construyo clave compuesta por id de carrito y id de producto
        CarritoProductoId id = new CarritoProductoId(idCarrito, productoSeleccionado.getIdProducto());

        //Luego construyo la entity
        CarritoProductoEntity carritoProductoEntity = new CarritoProductoEntity();
        ProductoEntity productoEntity = productoSvc.findById(productoSeleccionado.getIdProducto());
        carritoProductoEntity.setId(id);
        carritoProductoEntity.setCarrito(this.findById(idCarrito));
        carritoProductoEntity.setProducto(productoEntity);
        carritoProductoEntity.setCantidad(productoSeleccionado.getCantidad());
        carritoProductoEntity.setEnPromocion(productoEntity.isEnPromocion());
        //van a estar los precios sin descuento todavía
        carritoProductoEntity.setSubtotal(productoEntity.getPrecio() * productoSeleccionado.getCantidad());
        carritoProductoDao.save(carritoProductoEntity);

        //Modifico el estado del carrito que ahora al tener un producto pasa a estar abierto
        if (!carritoEntity.getCarritoProductos().isEmpty())
            carritoEntity = this.actualizarEstadoDelCarrito(carritoEntity, ESTADO_ABIERTO);

        return mapper.entityToDto(carritoEntity);
    }

    public MensajeDTO finalizarCarrito(Long idCarrito){
        Thread procesoAsincrono = new Thread(() ->
                procesamientoSvc.finalizarCarrito(idCarrito));
        procesoAsincrono.start();
        return new MensajeDTO(MensajeDTO.MENSAJE_PROCESO);
    }
    public CarritoDTO eliminarProductoDelCarrito(Long idCarrito, ProductoSeleccionadoDTO producto){
        CarritoProductoId id = new CarritoProductoId(idCarrito, producto.getIdProducto());
        try {
            carritoProductoDao.deleteById(id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EntityNotFoundException("No se encontró el producto " + producto.getIdProducto() +" para borrar del carrito " + idCarrito);
        }
        return mapper.entityToDto(this.findById(idCarrito));
    }

    public CarritoDTO eliminarUnidadDeProductoDelCarrito(Long idCarrito, ProductoSeleccionadoDTO producto){
        CarritoEntity carritoEntity = this.findById(idCarrito);
        if(carritoEntity.getEstado().getDescripcion().equals("VACIO") || carritoEntity.getEstado().getDescripcion().equals("PROCESADO"))
            throw new RuntimeException("No se puede borrar productos de un carrito vacio o procesado");

        CarritoProductoId id = new CarritoProductoId(idCarrito, producto.getIdProducto());
        CarritoProductoEntity carritoProductoEntity = carritoProductoDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró CarritoProducto con id " + idCarrito + "-" + producto.getIdProducto()));

        //Resto 1 unidad a la cantidad
        Integer cantidad = carritoProductoEntity.getCantidad();


        if (cantidad > 1) {
            Double nuevoSubtotal = this.calcularNuevoSubtotal(cantidad, carritoProductoEntity.getSubtotal());
            carritoProductoEntity.setCantidad(cantidad - 1);
            carritoProductoEntity.setSubtotal(nuevoSubtotal);
            carritoProductoDao.save(carritoProductoEntity);
        } else {
            this.eliminarProductoDelCarrito(idCarrito, producto);
            carritoEntity.setEstado(carritoEstadoSvc.findById(ESTADO_VACIO));
            carritoDao.save(carritoEntity);
        }

        return mapper.entityToDto(carritoEntity);
    }
    public List<CarritoProductoDTO> obtenerTodosLosProductosDeUnCarrito(Long idCarrito){
        List<CarritoProductoEntity> productos = this.findById(idCarrito).getCarritoProductos();
        return carritoProductoMapper.entitiesToDtos(productos);
    }

    public void guardarCarrito(CarritoEntity entity){
        carritoDao.save(entity);
    }

    private Double calcularNuevoSubtotal(Integer cantidad, double subtotalOriginal){
        return subtotalOriginal - (subtotalOriginal / cantidad);
    }

    private CarritoEntity actualizarEstadoDelCarrito(CarritoEntity entity, Long estado){
        entity.setEstado(carritoEstadoSvc.findById(estado));
        return carritoDao.save(entity);
    }
}
