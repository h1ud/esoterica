package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.domain.entity.Username;
import com.webproject.esoteria.domain.mapper.userMapper;
import com.webproject.esoteria.repository.roleRepository;
import com.webproject.esoteria.repository.usuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class userService {
    private final usuarioRepository usuarioRepository;
    private final roleRepository roleRepository;
    private final userMapper userMapper;

    public userService(usuarioRepository usuarioRepository, roleRepository roleRepository, userMapper userMapper) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    public userDTO create(userDTO dto) {
        Username username = userMapper.toEntity(dto);
        username.setCreate_date(dto.getCreate_date() != null ? dto.getCreate_date() : LocalDateTime.now());
        username.setRole(resolveRole(dto.getRole()));

        return userMapper.toDto(usuarioRepository.save(username));
    }

    public userDTO getById(Long id) {
        return usuarioRepository.findById(id).map(userMapper::toDto).orElse(null);
    }

    public userDTO delete(Long id) {
        return usuarioRepository.findById(id)
                .map(username -> {
                    userDTO deleted = userMapper.toDto(username);
                    usuarioRepository.delete(username);
                    return deleted;
                })
                .orElse(null);
    }

    public userDTO update(Long id, userDTO dto) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    userMapper.updateEntityFromDTO(dto, existing);
                    if (dto.getRole() != null) {
                        existing.setRole(resolveRole(dto.getRole()));
                    }
                    return userMapper.toDto(usuarioRepository.save(existing));
                })
                .orElse(null);
    }

    public List<userDTO> listAll() {
        return usuarioRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    private Role resolveRole(Role requestedRole) {
        if (requestedRole != null && requestedRole.getId() > 0) {
            return roleRepository.findById(requestedRole.getId()).orElseGet(this::defaultUserRole);
        }

        if (requestedRole != null && requestedRole.getRole_name() != null) {
            return roleRepository.findAll().stream()
                    .filter(role -> requestedRole.getRole_name().equalsIgnoreCase(role.getRole_name()))
                    .findFirst()
                    .orElseGet(this::defaultUserRole);
        }

        return defaultUserRole();
    }

    private Role defaultUserRole() {
        return roleRepository.findAll().stream()
                .filter(role -> "USER".equalsIgnoreCase(role.getRole_name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No existe el rol USER en la base de datos"));
    }
}
