package com.challange.service;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.dto.responses.CarritoDTO;
import com.challange.entity.*;
import com.challange.entity.id.CarritoProductoId;
import com.challange.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CarritoServiceIntegrationTest {

    @Autowired
    private EstadoCarritoRepository estadoCarritoDao;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoCarritoRepository estadoCarritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private CarritoRepository carritoRepository;
    @Test
    void crearCarrito_DeberiaCrearCarritoAsociadoACliente(){
        EstadoCarritoEntity estadoCarritoEntity = new EstadoCarritoEntity();
        estadoCarritoEntity.setDescripcion("ABIERTO");

        estadoCarritoDao.save(estadoCarritoEntity);

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNombre("INTEGRATION");
        clienteEntity.setApellido("TEST");

        clienteEntity = clienteRepository.save(clienteEntity);

        CarritoDTO carritoDTO = carritoService.crearCarrito(clienteEntity.getId());
        CarritoEntity carritoEntity = carritoService.findById(1L);
        assertThat(carritoDTO).isNotNull();
        assertThat(carritoDTO.getCliente().getApellido()).isEqualTo("TEST");
        assertThat(carritoDTO.getCliente().getNombre()).isEqualTo("INTEGRATION");
        assertThat(carritoEntity).isNotNull();
        assertThat(carritoEntity.getCliente().getId()).isEqualTo(clienteEntity.getId());
    }

    @Test
    @Transactional
    void eliminarProducto_eliminarUnidadDeProductoConMasDeUnaUnidadDebeSeguirTeniendoProducto(){
        Long idCarrito = 1L;
        Long idProducto = 1L;

        ProductoSeleccionadoDTO productoSeleccionadoDto = new ProductoSeleccionadoDTO();
        productoSeleccionadoDto.setIdProducto(idProducto);
        productoSeleccionadoDto.setCantidad(2);

        ProductoEntity productoEntity = productoRepository.findById(idProducto).orElseThrow(() -> new EntityNotFoundException(""));

        CarritoEntity carritoEntity = new CarritoEntity();
        carritoEntity.setCarritoId(idCarrito);
        carritoEntity.setEstado(estadoCarritoRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("")));
        carritoEntity.setCarritoProductos(new ArrayList<>());
        CarritoEntity entity = carritoRepository.save(carritoEntity);
        CarritoProductoId id = new CarritoProductoId(idCarrito, idProducto);


        CarritoProductoEntity carritoProductoEntity = new CarritoProductoEntity();
        carritoProductoEntity.setProducto(productoEntity);
        carritoProductoEntity.setCarrito(carritoEntity);
        carritoProductoEntity.setCantidad(productoSeleccionadoDto.getCantidad());
        carritoProductoEntity.setEnPromocion(false);
        carritoProductoEntity.setSubtotal(2400.0);
        carritoProductoEntity.setId(id);
        carritoProductoRepository.save(carritoProductoEntity);

        List<CarritoProductoEntity> productos = new ArrayList<>();
        productos.add(carritoProductoEntity);

        carritoEntity.setCarritoProductos(productos);
        carritoRepository.save(carritoEntity);

        CarritoDTO result = carritoService.eliminarUnidadDeProductoDelCarrito(idCarrito, productoSeleccionadoDto);
        System.out.println(result.getCarritoProductos().size());
        assertThat(result.getEstado().getDescripcion()).isEqualTo("ABIERTO");
        assertThat(result.getCarritoProductos().get(0).getProducto().getDescripcion()).isEqualTo("IPHONE 15");
        assertThat(result.getCarritoProductos().size()).isEqualTo(1);
    }
}
