package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.userDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.annotation.PostConstruct;

@Service
public class userService {

    // Reemplazo de uso de repositorio por almacenamiento en memoria
    private final Map<Long, userDTO> store = new ConcurrentHashMap<>();
    private final Map<String, Long> usernameToId = new ConcurrentHashMap<>();
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
        dto.setId(id);
        dto.setCreate_date(new Date());
        userDTO stored = cloneDto(dto);
        stored.setId(id);
        store.put(id, stored);
        if (dto.getUsername() != null) {
            usernameToId.put(dto.getUsername(), id);
        }
        return cloneDto(stored);
    }

    public Long getIdByUsername(String username) {
        return usernameToId.get(username);
    }

    public userDTO getById(Long id) {
        userDTO found = store.get(id);
        return found != null ? cloneDto(found) : null;
    }

    public userDTO delete(Long id) {
        userDTO removed = store.remove(id);
        if (removed != null && removed.getUsername() != null) usernameToId.remove(removed.getUsername());
        return removed != null ? cloneDto(removed) : null;
    }

    public userDTO update(Long id, userDTO dto) {
        return store.computeIfPresent(id, (k, existing) -> {
            // actualizar campos relevantes
            existing.setUsername(dto.getUsername() != null ? dto.getUsername() : existing.getUsername());
            existing.setPassword_hash(dto.getPassword_hash() != null ? dto.getPassword_hash() : existing.getPassword_hash());
            existing.setFirst_name(dto.getFirst_name() != null ? dto.getFirst_name() : existing.getFirst_name());
            existing.setLast_name(dto.getLast_name() != null ? dto.getLast_name() : existing.getLast_name());
            // no tocar create_date salvo que se necesite
            if (existing.getUsername() != null) usernameToId.put(existing.getUsername(), id);
            return existing;
        }) != null ? cloneDto(store.get(id)) : null;
    }

    public List<userDTO> listAll(){
        return store.entrySet().stream()
                .map(e -> {
                    userDTO c = cloneDto(e.getValue());
                    c.setId(e.getKey());
                    return c;
                })
                .toList();
    }

    // Helper para evitar compartir referencias mutables
    private userDTO cloneDto(userDTO src) {
        if (src == null) return null;
        userDTO copy = new userDTO();
        copy.setId(src.getId());
        copy.setUsername(src.getUsername());
        copy.setPassword_hash(src.getPassword_hash());
        copy.setFirst_name(src.getFirst_name());
        copy.setLast_name(src.getLast_name());
        copy.setCreate_date(src.getCreate_date() != null ? (Date) src.getCreate_date().clone() : null);
        return copy;
    }
}
