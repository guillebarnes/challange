package com.challange.repository;

import com.challange.entity.ProductoEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository<ProductoEntity, Long> {
}
