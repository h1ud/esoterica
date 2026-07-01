package com.webproject.esoteria.domain.dto;

import java.time.LocalDateTime;

public record PasswordResetDTO(
        Long id,
        String name,
        String lastName,
        String username,
        String email,
        LocalDateTime createdAt
) {}