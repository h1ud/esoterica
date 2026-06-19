package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.LoginRequestDTO;
import com.webproject.esoteria.domain.dto.LoginResponseDTO;
import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final userService userService;
    private final String bootstrapUsername;
    private final String bootstrapPassword;

    public AuthService(JwtService jwtService, userService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.bootstrapUsername = readEnv("ESOTERICA_ADMIN_USERNAME", "admin");
        this.bootstrapPassword = readEnv("ESOTERICA_ADMIN_PASSWORD", "admin123");
    }

    public Optional<LoginResponseDTO> login(LoginRequestDTO request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Optional.empty();
        }

        // Un solo login valida credenciales contra usuarios en BD; el rol viaja dentro del JWT.
        Optional<userDTO> authenticatedUser = findUserByCredentials(request);
        if (authenticatedUser.isPresent()) {
            userDTO user = authenticatedUser.get();
            return createLoginResponse(user.getUsername(), roleNameOrDefault(user));
        }

        // Fallback de arranque: permite entrar como admin solo si aun no existe ningun admin en BD.
        if (!existsAdminUser() && sameBootstrapCredentials(request)) {
            return createLoginResponse(request.getUsername(), "ADMIN");
        }

        return Optional.empty();
    }

    private Optional<userDTO> findUserByCredentials(LoginRequestDTO request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return Optional.empty();
        }

        return userService.listAll().stream()
                .filter(user -> sameCredentials(user, request))
                .findFirst();
    }

    private Optional<LoginResponseDTO> createLoginResponse(String username, String roleName) {
        String token = jwtService.createToken(username, roleName);
        return Optional.of(new LoginResponseDTO(
                token,
                jwtService.getTokenType(),
                jwtService.getExpirationSeconds(),
                username,
                roleName
        ));
    }

    private boolean sameCredentials(userDTO user, LoginRequestDTO request) {
        return user != null
                && request.getUsername().equals(user.getUsername())
                && request.getPassword().equals(user.getPassword_hash());
    }

    private boolean sameBootstrapCredentials(LoginRequestDTO request) {
        return bootstrapUsername.equals(request.getUsername())
                && bootstrapPassword.equals(request.getPassword());
    }

    private boolean existsAdminUser() {
        return userService.listAll().stream()
                .map(userDTO::getRole)
                .anyMatch(this::isAdminRole);
    }

    private boolean isAdminRole(Role role) {
        return role != null && jwtService.isAdminRoleName(role.getRole_name());
    }

    private String roleNameOrDefault(userDTO user) {
        Role role = user.getRole();
        return role != null && role.getRole_name() != null ? role.getRole_name() : "USER";
    }

    private String readEnv(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
