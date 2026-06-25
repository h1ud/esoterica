package com.webproject.esoteria.domain.dto.auth;

public record AuthResponseDTO(
        String token,
        String username,
        String roleName
) {
}
