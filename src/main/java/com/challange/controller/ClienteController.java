package com.challange.controller;

import com.challange.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ClienteController {

    @Autowired
    private ClienteRepository dao;
    @GetMapping("/")
    public ResponseEntity<?> obtenerTodosLosUsuarios(){
        return ResponseEntity.ok().body(dao.findAll());
    }
}
