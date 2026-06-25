package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // LISTAR ALL
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // CREAR ONE
    public Role saveRole(Role role) {
        // Validación opcional: pasar a mayúsculas para mantener consistencia (ej: ADMIN)
        role.setRoleName(role.getRoleName().toUpperCase().trim());

        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new IllegalArgumentException("El rol '" + role.getRoleName() + "' ya existe.");
        }
        return roleRepository.save(role);
    }

    // BORRAR ONE
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("El rol con ID " + id + " no existe.");
        }
        roleRepository.deleteById(id);
    }
}
