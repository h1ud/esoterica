package com.webproject.esoteria.domain.dto;

public record UserSaveDTO(
        Long idRole,
        String username,
        String password,
        String name,
        String lastName
) {
}
