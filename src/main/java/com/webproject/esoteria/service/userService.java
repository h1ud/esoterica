package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.userDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.annotation.PostConstruct;

@Service
public class userService {

    // Reemplazo de uso de repositorio por almacenamiento en memoria
    private final Map<Long, userDTO> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    private void init() {
        userDTO u1 = new userDTO();
        u1.setUsername("alice");
        u1.setPassword_hash("password1");
        u1.setFirst_name("Alice");
        u1.setLast_name("Garcia");
        create(u1);

        userDTO u2 = new userDTO();
        u2.setUsername("bob");
        u2.setPassword_hash("password2");
        u2.setFirst_name("Bob");
        u2.setLast_name("Lopez");
        create(u2);
    }

    public userDTO create(userDTO dto) {
        long id = idGenerator.getAndIncrement();
        dto.setCreate_date(LocalDateTime.now());
        store.put(id, cloneDto(dto));
        return cloneDto(dto);
    }

    public userDTO getById(Long id) {
        userDTO found = store.get(id);
        return found != null ? cloneDto(found) : null;
    }

    public userDTO delete(Long id) {
        userDTO removed = store.remove(id);
        return removed != null ? cloneDto(removed) : null;
    }

    public userDTO update(Long id, userDTO dto) {
        return store.computeIfPresent(id, (k, existing) -> {
            // actualizar campos relevantes
            existing.setUsername(dto.getUsername() != null ? dto.getUsername() : existing.getUsername());
            existing.setPassword_hash(dto.getPassword_hash() != null ? dto.getPassword_hash() : existing.getPassword_hash());
            existing.setFirst_name(dto.getFirst_name() != null ? dto.getFirst_name() : existing.getFirst_name());
            existing.setLast_name(dto.getLast_name() != null ? dto.getLast_name() : existing.getLast_name());
            existing.setRole(dto.getRole() != null ? dto.getRole() : existing.getRole());
            // no tocar create_date salvo que se necesite
            return existing;
        }) != null ? cloneDto(store.get(id)) : null;
    }

    public List<userDTO> listAll(){
        return store.values().stream().map(this::cloneDto).toList();
    }

    // Helper para evitar compartir referencias mutables
    private userDTO cloneDto(userDTO src) {
        if (src == null) return null;
        userDTO copy = new userDTO();
        copy.setUsername(src.getUsername());
        copy.setPassword_hash(src.getPassword_hash());
        copy.setFirst_name(src.getFirst_name());
        copy.setLast_name(src.getLast_name());
        copy.setRole(src.getRole());
        copy.setCreate_date(src.getCreate_date());
        return copy;
    }
}
