package com.webproject.esoteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.webproject.esoteria.domain.dto.roleDTO;
import com.webproject.esoteria.service.roleService;

@RestController
@RequestMapping("/api/roles")
public class roleController {

    @Autowired
    private roleService roleService;

    @PostMapping
    public ResponseEntity<roleDTO> createRole(@RequestBody roleDTO roleDTO) {
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<roleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<List<roleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<roleDTO> updateRole(@PathVariable Long id, @RequestBody roleDTO roleDTO) {
        roleDTO updatedRole = roleService.updateRole(id, roleDTO);
        return updatedRole != null
                ? ResponseEntity.ok(updatedRole)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();  // Respuesta 204 sin contenido
    }
}
