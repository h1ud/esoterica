package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.roleDTO;
import com.webproject.esoteria.service.AdminJwtService;
import com.webproject.esoteria.service.roleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {
    private final roleService roleService;
    private final AdminJwtService adminJwtService;

    public AdminRoleController(roleService roleService, AdminJwtService adminJwtService) {
        this.roleService = roleService;
        this.adminJwtService = adminJwtService;
    }

    @PostMapping
    public ResponseEntity<roleDTO> createRole(
            @RequestBody roleDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // Reutiliza roleService, pero exige el mismo JWT admin que el CRUD de usuarios.
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<roleDTO> getRoleById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        roleDTO role = roleService.getRoleById(id);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<roleDTO>> getAllRoles(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<roleDTO> updateRole(
            @PathVariable Long id,
            @RequestBody roleDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        roleDTO updatedRole = roleService.updateRole(id, request);
        return updatedRole != null ? ResponseEntity.ok(updatedRole) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isUnauthorized(String authorization) {
        return !adminJwtService.isValidAuthorizationHeader(authorization);
    }
}
