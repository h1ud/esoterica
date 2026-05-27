package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.service.JwtService;
import com.webproject.esoteria.service.userService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/admin")
public class AdminController {
    private final JwtService jwtService;
    private final userService userService;

    public AdminController(JwtService jwtService, userService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<userDTO>> listUsers(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // La gestion de usuarios queda centralizada aqui y protegida por JWT con rol ADMIN.
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        return ResponseEntity.ok(userService.listAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<userDTO> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        userDTO user = userService.getById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<userDTO> createUser(
            @Valid @RequestBody userDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<userDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody userDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        userDTO updatedUser = userService.update(id, request);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        userDTO deletedUser = userService.delete(id);
        return deletedUser != null ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private HttpStatus resolveAdminAccessError(String authorization) {
        // 401 = no se autentico con JWT valido. 403 = si tiene JWT, pero no tiene rol ADMIN.
        if (!jwtService.isValidAuthorizationHeader(authorization)) {
            return HttpStatus.UNAUTHORIZED;
        }

        return jwtService.isValidAdminAuthorizationHeader(authorization) ? null : HttpStatus.FORBIDDEN;
    }
}
