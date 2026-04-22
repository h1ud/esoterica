package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.roleDTO;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class roleService {

    private final Map<String, roleDTO> store = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        // no sembrar aquí para evitar duplicados si se usa CommandLineRunner; dejar como fallback opcional
        // roleDTO r1 = new roleDTO(); r1.setName("ADMIN"); r1.setDescription("Administrador"); create(r1);
        // roleDTO r2 = new roleDTO(); r2.setName("USER"); r2.setDescription("Usuario"); create(r2);
    }

    public roleDTO create(roleDTO dto) {
        if (dto == null || dto.getName() == null) return null;
        store.put(dto.getName(), dto);
        return dto;
    }

    public roleDTO getByName(String name) {
        return store.get(name);
    }

    public List<roleDTO> listAll() {
        return new ArrayList<>(store.values());
    }
}
