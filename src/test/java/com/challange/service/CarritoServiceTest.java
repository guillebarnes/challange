package com.challange.service;

import com.challange.entity.CarritoEntity;
import com.challange.entity.EstadoCarritoEntity;
import com.challange.entity.ClienteEntity;
import com.challange.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoEstadoService carritoEstadoSvc;
    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ClienteService usuarioSvc;
    @InjectMocks
    private CarritoService carritoSvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCarrito_DeberiaCrearCarritoAsociadoAUsuario() {
/*
        Long usuarioId = 1L;

        ClienteEntity usuario = new ClienteEntity();
        usuario.setId(usuarioId);
        usuario.setNombre("GUILLERMO");
        usuario.setApellido("BARNES");

        EstadoCarritoEntity estadoMock = new EstadoCarritoEntity();
        estadoMock.setId(1L);
        estadoMock.setDescripcion("ABIERTO");
        CarritoEntity carritoGuardado = new CarritoEntity();
        carritoGuardado.setCarritoId(100L);
        carritoGuardado.setCliente(usuario);

        when(usuarioSvc.findById(usuarioId))
                .thenReturn(usuario);
        when(carritoRepository.save(any(CarritoEntity.class)))
                .thenReturn(carritoGuardado);

        when(carritoEstadoSvc.findById(1L))
                .thenReturn(estadoMock);
        //CarritoEntity result = carritoSvc.crearCarrito(usuarioId);

        assertThat(result).isNotNull();
        assertThat(result.getCliente().getId()).isEqualTo(usuarioId);
        assertThat(result.getCarritoId()).isEqualTo(100L);

        verify(carritoRepository, times(1)).save(any(CarritoEntity.class));*/
    }

}
