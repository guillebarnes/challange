package com.challange.service;

import com.challange.entity.ClienteEntity;
import com.challange.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository dao;

    public ClienteEntity findById(Long id){
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró UsuarioEntity con id " + id));
    }

    public ClienteEntity crearUsuario(String nombre, String apellido){
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(nombre);
        entity.setApellido(apellido);
        return dao.save(entity);
    }
}
