package com.webproject.esoteria.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductSaveDTO(
        @NotBlank
        String productName,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        Long categoryId
) {
}
