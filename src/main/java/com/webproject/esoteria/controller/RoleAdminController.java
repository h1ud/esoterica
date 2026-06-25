package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RoleAdminController {
    @Autowired
    private RoleService roleService;

    // 1. LISTAR ROLES
    @GetMapping
    public ResponseEntity<List<Role>> listRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // 2. CREAR ROL
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role newRole = roleService.saveRole(role);
            return new ResponseEntity<>(newRole, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. BORRAR ROL
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build(); // Devuelve 24 No Content si se borra con éxito
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
