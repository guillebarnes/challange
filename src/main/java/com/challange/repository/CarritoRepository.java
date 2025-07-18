package com.challange.repository;

import com.challange.entity.CarritoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarritoRepository extends CrudRepository<CarritoEntity, Long> {
    @Query(value = "SELECT * FROM carrito WHERE cliente_id = :idCliente", nativeQuery = true)
    public List<CarritoEntity> obtenerCarritosPorCliente(@Param("idCliente") Long idCliente);
}
