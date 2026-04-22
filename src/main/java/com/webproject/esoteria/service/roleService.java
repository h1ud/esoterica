package com.webproject.esoteria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webproject.esoteria.domain.dto.roleDTO;
import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.repository.roleRepository;

@Service
public class roleService {

    @Autowired
    private roleRepository roleRepository;

    public roleDTO createRole(roleDTO roleDTO) {
        Role role = new Role(roleDTO.getRole_name());
        Role savedRole = roleRepository.save(role);
        return new roleDTO(savedRole.getId(), savedRole.getRole_name());
    }

    public roleDTO getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(role -> new roleDTO(role.getId(), role.getRole_name()))
                .orElse(null);
    }

    public List<roleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new roleDTO(role.getId(), role.getRole_name()))
                .collect(Collectors.toList());
    }

    public roleDTO updateRole(Long id, roleDTO roleDTO) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setRole_name(roleDTO.getRole_name());
                    Role updatedRole = roleRepository.save(role);
                    return new roleDTO(updatedRole.getId(), updatedRole.getRole_name());
                })
                .orElse(null);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
