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
import com.challange.service.state.EstadoCarrito;
import com.challange.service.state.EstadoCarritoFactory;
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
    @Autowired
    private EstadoCarritoFactory estadoCarritoFactory;

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

        EstadoCarrito estado = estadoCarritoFactory.obtenerEstado(carritoEntity.getEstado().getDescripcion());

        return mapper.entityToDto(estado.agregarProducto(carritoEntity, productoSeleccionado));
    }

    public MensajeDTO finalizarCarrito(Long idCarrito){
        CarritoEntity carritoEntity = this.findById(idCarrito);

        EstadoCarrito estado = estadoCarritoFactory.obtenerEstado(carritoEntity.getEstado().getDescripcion());
        estado.finalizar(idCarrito);
        return new MensajeDTO(MensajeDTO.MENSAJE_PROCESO);
    }

    public CarritoDTO eliminarProductoDelCarrito(Long idCarrito, ProductoSeleccionadoDTO producto){
        CarritoEntity carritoEntity = this.findById(idCarrito);
        EstadoCarrito estado = estadoCarritoFactory.obtenerEstado(carritoEntity.getEstado().getDescripcion());
        carritoEntity = estado.eliminarProducto(carritoEntity, producto);
        return mapper.entityToDto(carritoEntity);
    }

    public CarritoDTO eliminarUnidadDeProductoDelCarrito(Long idCarrito, ProductoSeleccionadoDTO producto){
        CarritoEntity carritoEntity = this.findById(idCarrito);

        EstadoCarrito estado = estadoCarritoFactory.obtenerEstado(carritoEntity.getEstado().getDescripcion());

        return mapper.entityToDto(estado.eliminarUnidadDeProducto(carritoEntity, producto));
    }
    public List<CarritoProductoDTO> obtenerTodosLosProductosDeUnCarrito(Long idCarrito){
        List<CarritoProductoEntity> productos = this.findById(idCarrito).getCarritoProductos();
        return carritoProductoMapper.entitiesToDtos(productos);
    }

    public CarritoEntity guardarCarrito(CarritoEntity entity){
        return carritoDao.save(entity);
    }

    public Double calcularNuevoSubtotal(Integer cantidad, double subtotalOriginal){
        return subtotalOriginal - (subtotalOriginal / cantidad);
    }

    public CarritoEntity actualizarEstadoDelCarrito(CarritoEntity entity, Long estado){
        entity.setEstado(carritoEstadoSvc.findById(estado));
        return carritoDao.save(entity);
    }
}
