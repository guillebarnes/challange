package com.challange.service;

import com.challange.entity.CarritoEntity;
import com.challange.entity.CarritoProductoEntity;
import com.challange.repository.CarritoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcesamientoService {

    private static Long ESTADO_FINALIZADO = 2L;
    @Autowired
    private CarritoService carritoSvc;

    @Autowired
    private CarritoEstadoService estadoSvc;

    @Autowired
    private CarritoProductoRepository carritoProductoDao;
    public void finalizarCarrito(Long idCarrito){
        try {
            Thread.sleep(10000L);
            List<CarritoProductoEntity> productos = carritoProductoDao.obtenerTodosLosProductosDeUnCarrito(idCarrito);
            this.aplicarDescuentos(productos);
            CarritoEntity carritoEntity = carritoSvc.findById(idCarrito);
            carritoEntity.setCarritoProductos(productos);
            carritoEntity.setEstado(estadoSvc.findById(ESTADO_FINALIZADO));
            carritoSvc.guardarCarrito(carritoEntity);
        }catch (Exception e){
            System.out.println("error: " + e.getMessage());
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
