package com.challange.service;

import com.challange.dto.requests.ProductoSeleccionadoDTO;
import com.challange.dto.responses.CarritoDTO;
import com.challange.dto.responses.CarritoProductoDTO;
import com.challange.dto.responses.ClienteDTO;
import com.challange.dto.responses.EstadoCarritoDTO;
import com.challange.entity.*;
import com.challange.entity.id.CarritoProductoId;
import com.challange.mapper.CarritoMapper;
import com.challange.repository.CarritoProductoRepository;
import com.challange.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoEstadoService carritoEstadoSvc;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private ClienteService clienteSvc;
    @InjectMocks
    private CarritoService carritoSvc;
    @Mock
    private ProductoService productoSvc;
    @Mock
    private CarritoMapper carritoMapper;
    @Mock
    private CarritoProductoRepository carritoProductoDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCarrito_DeberiaCrearCarritoAsociadoACliente() {
        Long idCarrito = 1L;
        Long idCliente= 1L;
        Long idEstadoCarrito = 1L;

        ClienteEntity clienteEntityMock = new ClienteEntity();
        clienteEntityMock.setId(idCliente);
        clienteEntityMock.setNombre("TEST");
        clienteEntityMock.setApellido("UNITARIO");

        EstadoCarritoEntity estadoEntityMock = new EstadoCarritoEntity();
        estadoEntityMock.setId(idEstadoCarrito);
        estadoEntityMock.setDescripcion("ABIERTO");

        CarritoEntity carritoEntityMock = new CarritoEntity();
        carritoEntityMock.setCarritoId(idCarrito);
        carritoEntityMock.setCliente(clienteEntityMock);

        ClienteDTO clienteDtoMock = new ClienteDTO();
        clienteDtoMock.setApellido("UNITARIO");

        CarritoDTO carritoDtoMock = new CarritoDTO();
        carritoDtoMock.setCarritoId(idCarrito);
        carritoDtoMock.setCliente(clienteDtoMock);

        when(clienteSvc.findById(idCliente)).thenReturn(clienteEntityMock);
        when(carritoEstadoSvc.findById(idEstadoCarrito)).thenReturn(estadoEntityMock);
        when(carritoMapper.entityToDto(any())).thenReturn(carritoDtoMock);

        CarritoDTO result = carritoSvc.crearCarrito(idCliente);

        assertThat(result).isNotNull();
        assertThat(result.getCarritoId()).isEqualTo(1L);
        assertThat(result.getCliente().getApellido()).isEqualTo("UNITARIO");
    }

    @Test
    void agregarProducto_laListaDeProductosTieneQueTenerUnProducto(){
        Long idCarrito = 1L;
        Long idProducto = 1L;

        ProductoSeleccionadoDTO productoSeleccionadoDTO = new ProductoSeleccionadoDTO();
        productoSeleccionadoDTO.setIdProducto(idProducto);
        productoSeleccionadoDTO.setCantidad(1);

        //Mockeo las entities
        EstadoCarritoEntity estadoCarritoEntity = new EstadoCarritoEntity();
        estadoCarritoEntity.setDescripcion("ABIERTO");

        ProductoEntity productoMock = new ProductoEntity();
        productoMock.setProductoId(idProducto);
        productoMock.setPrecio(100.0);
        productoMock.setEnPromocion(false);

        CarritoEntity carritoMock = new CarritoEntity();
        carritoMock.setEstado(estadoCarritoEntity);
        carritoMock.setCarritoId(idCarrito);
        carritoMock.setCarritoProductos(new ArrayList<>());

        //Creo la lista de productos de un carrito
        CarritoProductoDTO carritoProductoDtoMock = new CarritoProductoDTO();
        List<CarritoProductoDTO> carritoProductos = new ArrayList<>();
        carritoProductos.add(carritoProductoDtoMock);
        CarritoDTO carritoDtoMock = new CarritoDTO();
        carritoDtoMock.setCarritoProductos(carritoProductos);

        when(productoSvc.findById(idProducto)).thenReturn(productoMock);
        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carritoMock));
        when(carritoMapper.entityToDto(any())).thenReturn(carritoDtoMock);

        CarritoDTO result = carritoSvc.agregarProductoACarrito(idCarrito, productoSeleccionadoDTO);

        assertThat(result.getCarritoProductos().size()).isEqualTo(1);
    }

    @Test
    void eliminarProducto_eliminarUnidadDeProductoConMasDeUnaCantidadDebeSeguirTeniendoProducto(){
        Long idCarrito = 1L;
        Long idProducto = 1L;

        ProductoSeleccionadoDTO productoSeleccionadoDTO = new ProductoSeleccionadoDTO();
        productoSeleccionadoDTO.setIdProducto(idProducto);

        //Mockeo las entities
        ProductoEntity productoMock = new ProductoEntity();
        productoMock.setProductoId(idProducto);
        productoMock.setPrecio(100.0);
        productoMock.setEnPromocion(false);

        EstadoCarritoEntity estadoCarritoEntity = new EstadoCarritoEntity();
        estadoCarritoEntity.setDescripcion("ABIERTO");

        CarritoEntity carritoMock = new CarritoEntity();
        carritoMock.setCarritoId(idCarrito);
        carritoMock.setCarritoProductos(new ArrayList<>());
        carritoMock.setEstado(estadoCarritoEntity);

        CarritoProductoEntity carritoProductoEntityMock = new CarritoProductoEntity();
        carritoProductoEntityMock.setId(new CarritoProductoId(idCarrito, idProducto));
        carritoProductoEntityMock.setCarrito(carritoMock);
        carritoProductoEntityMock.setProducto(productoMock);
        carritoProductoEntityMock.setCantidad(4);
        carritoProductoEntityMock.setSubtotal(400.0);
        carritoProductoEntityMock.setEnPromocion(false);

        List<CarritoProductoEntity> carritoProductoEntityList = new ArrayList<>();
        carritoProductoEntityList.add(carritoProductoEntityMock);

        carritoMock.setCarritoProductos(carritoProductoEntityList);

        CarritoDTO carritoDtoMock = new CarritoDTO();
        carritoDtoMock.setCarritoId(idCarrito);

        CarritoProductoDTO carritoProductoDto = new CarritoProductoDTO();
        carritoProductoDto.setCantidad(3);
        carritoProductoDto.setSubtotal(300.0);

        carritoDtoMock.setCarritoProductos(List.of(carritoProductoDto));

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carritoMock));
        when(carritoMapper.entityToDto(carritoMock)).thenReturn(carritoDtoMock);
        when(productoSvc.findById(idProducto)).thenReturn(productoMock);
        when(carritoProductoDao.findById(new CarritoProductoId(idCarrito,idProducto))).thenReturn(Optional.of(carritoProductoEntityMock));

        CarritoDTO result = carritoSvc.eliminarUnidadDeProductoDelCarrito(idCarrito, productoSeleccionadoDTO);

        assertThat(result.getCarritoProductos().size()).isEqualTo(1);
        assertThat(result.getCarritoProductos().get(0).getCantidad()).isEqualTo(3);
        assertThat(result.getCarritoProductos().get(0).getSubtotal()).isEqualTo(300.0);
    }

    @Test
    void eliminarProducto_eliminarUnidadDeProductoConUnaUnicaCantidadDebeSacarloDeLaLista(){
        Long idCarrito = 1L;
        Long idProducto = 1L;

        ProductoSeleccionadoDTO productoSeleccionadoDTO = new ProductoSeleccionadoDTO();
        productoSeleccionadoDTO.setIdProducto(idProducto);

        //Mockeo las entities
        ProductoEntity productoMock = new ProductoEntity();
        productoMock.setProductoId(idProducto);
        productoMock.setPrecio(100.0);
        productoMock.setEnPromocion(false);

        EstadoCarritoEntity estadoCarritoEntity = new EstadoCarritoEntity();
        estadoCarritoEntity.setId(1L);
        estadoCarritoEntity.setDescripcion("ABIERTO");

        CarritoEntity carritoMock = new CarritoEntity();
        carritoMock.setCarritoId(idCarrito);
        carritoMock.setCarritoProductos(new ArrayList<>());
        carritoMock.setEstado(estadoCarritoEntity);

        CarritoProductoEntity carritoProductoEntityMock = new CarritoProductoEntity();
        carritoProductoEntityMock.setId(new CarritoProductoId(idCarrito, idProducto));
        carritoProductoEntityMock.setCarrito(carritoMock);
        carritoProductoEntityMock.setProducto(productoMock);
        carritoProductoEntityMock.setCantidad(1);
        carritoProductoEntityMock.setSubtotal(100.0);
        carritoProductoEntityMock.setEnPromocion(false);

        List<CarritoProductoEntity> carritoProductoEntityList = new ArrayList<>();
        carritoProductoEntityList.add(carritoProductoEntityMock);

        carritoMock.setCarritoProductos(carritoProductoEntityList);

        EstadoCarritoDTO estadoCarritoDtoMock = new EstadoCarritoDTO();
        estadoCarritoDtoMock.setDescripcion("VACIO");

        CarritoDTO carritoDtoMock = new CarritoDTO();
        carritoDtoMock.setCarritoId(idCarrito);
        carritoDtoMock.setEstado(estadoCarritoDtoMock);
        carritoDtoMock.setCarritoProductos(new ArrayList<>());

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carritoMock));
        when(carritoMapper.entityToDto(carritoMock)).thenReturn(carritoDtoMock);
        when(productoSvc.findById(idProducto)).thenReturn(productoMock);
        when(carritoProductoDao.findById(new CarritoProductoId(idCarrito,idProducto))).thenReturn(Optional.of(carritoProductoEntityMock));

        CarritoDTO result = carritoSvc.eliminarUnidadDeProductoDelCarrito(idCarrito, productoSeleccionadoDTO);

        assertThat(result.getCarritoProductos().size()).isEqualTo(0);
        assertThat(result.getEstado().getDescripcion()).isEqualTo("VACIO");
        assertThat(result.getCarritoProductos()).isEmpty();
    }

    @Test
    void eliminarProducto_eliminarProductoEnteroDebeSacarloDeLaLista(){
        Long idCarrito = 1L;
        Long idProducto = 1L;

        ProductoSeleccionadoDTO productoSeleccionadoDTO = new ProductoSeleccionadoDTO();
        productoSeleccionadoDTO.setIdProducto(idProducto);

        //Mockeo las entities
        ProductoEntity productoMock = new ProductoEntity();
        productoMock.setProductoId(idProducto);
        productoMock.setPrecio(100.0);
        productoMock.setEnPromocion(false);

        CarritoEntity carritoMock = new CarritoEntity();
        carritoMock.setCarritoId(idCarrito);
        carritoMock.setCarritoProductos(new ArrayList<>());

        CarritoProductoEntity carritoProductoEntityMock = new CarritoProductoEntity();
        carritoProductoEntityMock.setId(new CarritoProductoId(idCarrito, idProducto));
        carritoProductoEntityMock.setCarrito(carritoMock);
        carritoProductoEntityMock.setProducto(productoMock);
        carritoProductoEntityMock.setCantidad(1);
        carritoProductoEntityMock.setSubtotal(100.0);
        carritoProductoEntityMock.setEnPromocion(false);

        List<CarritoProductoEntity> carritoProductoEntityList = new ArrayList<>();
        carritoProductoEntityList.add(carritoProductoEntityMock);

        carritoMock.setCarritoProductos(carritoProductoEntityList);

        CarritoDTO carritoDtoMock = new CarritoDTO();
        carritoDtoMock.setCarritoId(idCarrito);

        carritoDtoMock.setCarritoProductos(new ArrayList<>());

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carritoMock));
        when(carritoMapper.entityToDto(carritoMock)).thenReturn(carritoDtoMock);
        when(productoSvc.findById(idProducto)).thenReturn(productoMock);
        when(carritoProductoDao.findById(new CarritoProductoId(idCarrito,idProducto))).thenReturn(Optional.of(carritoProductoEntityMock));

        CarritoDTO result = carritoSvc.eliminarProductoDelCarrito(idCarrito, productoSeleccionadoDTO);

        assertThat(result.getCarritoProductos().size()).isEqualTo(0);
        assertThat(result.getCarritoProductos()).isEmpty();
    }


}
