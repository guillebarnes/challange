package com.challange.repository;

import com.challange.entity.EstadoCarritoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EstadoCarritoRepository extends CrudRepository<EstadoCarritoEntity, Long> {

}
