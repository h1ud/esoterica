package com.webproject.esoteria.domain.dto;

public record SaleItemRequestDTO(
        Long productId,
        Integer quantity
) {
}