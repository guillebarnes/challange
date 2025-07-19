package com.challange.service;

import com.challange.entity.*;
import com.challange.entity.id.CarritoProductoId;
import com.challange.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ProcesamientoServiceIntegrationTest {

    @Autowired
    private ProcesamientoService procesamientoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EstadoCarritoRepository estadoCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Test
    void procesarUnCarrito_aplicarDescuentoYCambiarDeEstado() {

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setDescuento(10L);
        categoria = categoriaRepository.save(categoria);

        ProductoEntity producto = new ProductoEntity();
        producto.setDescripcion("IPHONE");
        producto.setPrecio(1000.0);
        producto.setEnPromocion(true);
        producto.setCategoria(categoria);
        producto = productoRepository.save(producto);

        EstadoCarritoEntity estadoInicial = new EstadoCarritoEntity();
        estadoInicial.setDescripcion("ABIERTO");
        estadoInicial = estadoCarritoRepository.save(estadoInicial);

        EstadoCarritoEntity estadoFinal = new EstadoCarritoEntity();
        estadoFinal.setId(2L);
        estadoFinal.setDescripcion("PROCESADO");
        estadoFinal = estadoCarritoRepository.save(estadoFinal);

        CarritoEntity carrito = new CarritoEntity();
        carrito.setEstado(estadoInicial);
        carrito = carritoRepository.save(carrito);

        CarritoProductoEntity carritoProductoEntity = new CarritoProductoEntity();
        carritoProductoEntity.setId(new CarritoProductoId(carrito.getCarritoId(), producto.getProductoId()));
        carritoProductoEntity.setCarrito(carrito);
        carritoProductoEntity.setProducto(producto);
        carritoProductoEntity.setCantidad(1);
        carritoProductoEntity.setEnPromocion(true);
        carritoProductoEntity.setSubtotal(1000.0);
        carritoProductoRepository.save(carritoProductoEntity);

        procesamientoService.finalizarCarrito(carrito.getCarritoId());

        List<CarritoProductoEntity> procesados = carritoProductoRepository.obtenerTodosLosProductosDeUnCarrito(carrito.getCarritoId());
        assertEquals(1, procesados.size());
        assertEquals(900.0, procesados.get(0).getSubtotal(), 0.01);

        CarritoEntity carritoActualizado = carritoService.findById(carrito.getCarritoId());
        assertEquals(2L, carritoActualizado.getEstado().getId());
        assertEquals("PROCESADO", carritoActualizado.getEstado().getDescripcion());
    }
}
