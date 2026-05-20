package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.AdminLoginRequest;
import com.webproject.esoteria.domain.dto.AdminLoginResponse;
import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminAuthService {
    private final AdminJwtService adminJwtService;
    private final userService userService;
    private final String bootstrapUsername;
    private final String bootstrapPassword;

    public AdminAuthService(AdminJwtService adminJwtService, userService userService) {
        this.adminJwtService = adminJwtService;
        this.userService = userService;
        this.bootstrapUsername = readEnv("ESOTERICA_ADMIN_USERNAME", "admin");
        this.bootstrapPassword = readEnv("ESOTERICA_ADMIN_PASSWORD", "admin123");
    }

    public Optional<AdminLoginResponse> login(AdminLoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Optional.empty();
        }

        // Usa los usuarios existentes y permite entrar solo si tienen rol ADMIN o ROLE_ADMIN.
        Optional<userDTO> adminUser = userService.listAll().stream()
                .filter(user -> sameCredentials(user, request))
                .filter(user -> isAdminRole(user.getRole()))
                .findFirst();

        if (adminUser.isPresent()) {
            userDTO user = adminUser.get();
            return createLoginResponse(user.getUsername(), user.getRole().getRole_name());
        }

        // Permite arrancar el modulo admin cuando todavia no existe ningun usuario admin.
        if (!existsAdminUser() && sameBootstrapCredentials(request)) {
            return createLoginResponse(request.getUsername(), "ADMIN");
        }

        return Optional.empty();
    }

    private Optional<AdminLoginResponse> createLoginResponse(String username, String roleName) {
        String token = adminJwtService.createToken(username, roleName);
        return Optional.of(new AdminLoginResponse(
                token,
                adminJwtService.getTokenType(),
                adminJwtService.getExpirationSeconds()
        ));
    }

    private boolean sameCredentials(userDTO user, AdminLoginRequest request) {
        return user != null
                && request.getUsername().equals(user.getUsername())
                && request.getPassword().equals(user.getPassword_hash());
    }

    private boolean sameBootstrapCredentials(AdminLoginRequest request) {
        return bootstrapUsername.equals(request.getUsername())
                && bootstrapPassword.equals(request.getPassword());
    }

    private boolean existsAdminUser() {
        return userService.listAll().stream()
                .map(userDTO::getRole)
                .anyMatch(this::isAdminRole);
    }

    private boolean isAdminRole(Role role) {
        return role != null && adminJwtService.isAdminRoleName(role.getRole_name());
    }

    private String readEnv(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
