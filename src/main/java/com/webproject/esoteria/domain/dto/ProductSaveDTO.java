package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;

public record ProductSaveDTO(
        String productName,
        String description,
        BigDecimal price,
        Long categoryId
) {
}
