package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.AdminLoginRequest;
import com.webproject.esoteria.domain.dto.AdminLoginResponse;
import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.service.AdminAuthService;
import com.webproject.esoteria.service.AdminJwtService;
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
public class AdminUserController {
    private final AdminAuthService adminAuthService;
    private final AdminJwtService adminJwtService;
    private final userService userService;

    public AdminUserController(
            AdminAuthService adminAuthService,
            AdminJwtService adminJwtService,
            userService userService
    ) {
        this.adminAuthService = adminAuthService;
        this.adminJwtService = adminJwtService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        return adminAuthService.login(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/users")
    public ResponseEntity<List<userDTO>> listUsers(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // Estos endpoints usan una validacion JWT manual para no alterar la seguridad global del proyecto.
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userService.listAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<userDTO> getUserById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDTO user = userService.getById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<userDTO> createUser(
            @Valid @RequestBody userDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<userDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody userDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDTO updatedUser = userService.update(id, request);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        if (isUnauthorized(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userDTO deletedUser = userService.delete(id);
        return deletedUser != null ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean isUnauthorized(String authorization) {
        return !adminJwtService.isValidAuthorizationHeader(authorization);
    }
}
