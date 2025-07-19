package com.challange.service;

import com.challange.entity.*;
import com.challange.entity.id.CarritoProductoId;
import com.challange.repository.CarritoProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcesamientoServiceTest {

    @InjectMocks
    private ProcesamientoService procesamientoService;

    @Mock
    private CarritoService carritoSvc;

    @Mock
    private CarritoEstadoService estadoSvc;

    @Mock
    private CarritoProductoRepository carritoProductoDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void procesarUnCarrito_aplicarDescuentoYCambiarDeEstado() {

        Long idCarrito = 1L;
        Long descuento = 10L;

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setDescuento(descuento);

        ProductoEntity producto = new ProductoEntity();
        producto.setProductoId(1L);
        producto.setPrecio(1000.0);
        producto.setEnPromocion(true);
        producto.setCategoria(categoria);

        CarritoProductoEntity carritoProducto = new CarritoProductoEntity();
        carritoProducto.setId(new CarritoProductoId(1L, 1L));
        carritoProducto.setProducto(producto);
        carritoProducto.setCantidad(1);
        carritoProducto.setSubtotal(1000.0);
        carritoProducto.setEnPromocion(true);

        List<CarritoProductoEntity> productos = List.of(carritoProducto);

        CarritoEntity carritoEntity = new CarritoEntity();
        carritoEntity.setCarritoId(idCarrito);
        carritoEntity.setCarritoProductos(new ArrayList<>());

        EstadoCarritoEntity estadoProcesado = new EstadoCarritoEntity();
        estadoProcesado.setId(2L);
        estadoProcesado.setDescripcion("Procesado");

        when(carritoProductoDao.obtenerTodosLosProductosDeUnCarrito(idCarrito)).thenReturn(productos);
        when(carritoSvc.findById(idCarrito)).thenReturn(carritoEntity);
        when(estadoSvc.findById(2L)).thenReturn(estadoProcesado);

        procesamientoService.finalizarCarrito(idCarrito);

        assertEquals(900.0, productos.get(0).getSubtotal(), 0.01);
        assertEquals(estadoProcesado, carritoEntity.getEstado());

        verify(carritoSvc).guardarCarrito(carritoEntity);
    }
}
