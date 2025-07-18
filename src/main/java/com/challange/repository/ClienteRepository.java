package com.challange.repository;

import com.challange.entity.ClienteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends CrudRepository<ClienteEntity, Long> {

    @Query(value = "SELECT * FROM cliente WHERE user_id = :idUsuario", nativeQuery = true)
    public ClienteEntity findByUser(@Param("idUsuario") Long idUsuario);
}
