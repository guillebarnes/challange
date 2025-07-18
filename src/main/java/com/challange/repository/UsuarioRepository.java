package com.challange.repository;

import com.challange.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository  extends CrudRepository<UsuarioEntity, Long> {
    @Query(value = "SELECT * FROM USUARIO WHERE user = :user", nativeQuery = true)
    public UsuarioEntity findByUser(@Param("user") String user);
}
