package com.challange.service;

import com.challange.dto.responses.CarritoDTO;
import com.challange.entity.CarritoEntity;
import com.challange.entity.ClienteEntity;
import com.challange.entity.EstadoCarritoEntity;
import com.challange.repository.ClienteRepository;
import com.challange.repository.EstadoCarritoRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
}
