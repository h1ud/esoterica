package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.entity.Username;
import com.webproject.esoteria.repository.usuarioRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;


public class usuarioService {

    @Autowired
    private usuarioRepository usuarioRepository;
    public Username crearUser(Username user) {
        return usuarioRepository.save(user);
    }

    public Username obtenerUser(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
