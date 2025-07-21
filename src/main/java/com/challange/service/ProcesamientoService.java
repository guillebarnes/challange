package com.challange.service;

import com.challange.entity.CarritoEntity;
import com.challange.entity.CarritoProductoEntity;
import com.challange.repository.CarritoProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProcesamientoService {

    private static final Logger LOGGER = LoggerFactory.getLogger( ProcesamientoService.class );

    private static Long ESTADO_PROCESADO = 2L;
    @Autowired
    private CarritoService carritoSvc;

    @Autowired
    private CarritoEstadoService estadoSvc;

    @Autowired
    private CarritoProductoRepository carritoProductoDao;

    @Transactional
    public void finalizarCarrito(Long idCarrito){
        try {
            List<CarritoProductoEntity> productos = carritoProductoDao.obtenerTodosLosProductosDeUnCarrito(idCarrito);
            this.aplicarDescuentos(productos);
            CarritoEntity carritoEntity = carritoSvc.findById(idCarrito);
            carritoEntity.getCarritoProductos().clear();
            carritoEntity.getCarritoProductos().addAll(productos);
            carritoEntity.setEstado(estadoSvc.findById(ESTADO_PROCESADO));
            carritoSvc.guardarCarrito(carritoEntity);
        }catch (Exception e){
            LOGGER.error("Ocurrio un error al procesar el carrito {}. Motivo: {}", idCarrito, e.getMessage());
        }
    }
    public List<CarritoProductoEntity> aplicarDescuentos(List<CarritoProductoEntity> productos){
        for (CarritoProductoEntity item : productos) {
            if (item.isEnPromocion() && item.getProducto().isEnPromocion()) {
                Long descuento = item.getProducto().getCategoria().getDescuento(); // % de descuento
                double porcentaje = descuento / 100.0;
                double subtotalConDescuento = item.getSubtotal() * (1 - porcentaje);
                item.setSubtotal(subtotalConDescuento);
            }
        }

        return productos;
    }
}
