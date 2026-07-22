package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String productName,
        String description,
        BigDecimal price,
        boolean isAvailable,
        Long categoryId,
        String categoryName,
        String imageUrl
) {
}
