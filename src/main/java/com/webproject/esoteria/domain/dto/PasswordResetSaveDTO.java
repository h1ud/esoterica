package com.webproject.esoteria.domain.dto;

public record PasswordResetSaveDTO(
        String name,
        String lastName,
        String username,
        String email
) {}
