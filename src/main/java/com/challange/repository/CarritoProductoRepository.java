package com.challange.repository;

import com.challange.entity.CarritoProductoEntity;
import com.challange.entity.id.CarritoProductoId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarritoProductoRepository extends CrudRepository<CarritoProductoEntity, CarritoProductoId> {

    @Query(value = "SELECT * FROM CARRITO_PRODUCTO WHERE carrito_id = :idCarrito", nativeQuery = true)
    public List<CarritoProductoEntity> obtenerTodosLosProductosDeUnCarrito(@Param("idCarrito") Long idCarrito);
}
