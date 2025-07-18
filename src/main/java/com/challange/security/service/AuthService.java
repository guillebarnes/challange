package com.challange.security.service;

import com.challange.entity.ClienteEntity;
import com.challange.entity.UsuarioEntity;
import com.challange.repository.ClienteRepository;
import com.challange.repository.UsuarioRepository;
import com.challange.security.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteDao;

    public String login(String user, String pass) {
        UsuarioEntity usuario = usuarioRepository.findByUser(user);

        if (!usuario.getPass().equals(pass)) {
            throw new RuntimeException("Contraseña inválida");
        }

        ClienteEntity cliente = clienteDao.findByUser(usuario.getId());

        return JwtUtil.generateToken(user, cliente.getId());
    }
}
