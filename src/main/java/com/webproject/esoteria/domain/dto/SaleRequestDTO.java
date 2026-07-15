package com.webproject.esoteria.domain.dto;

import java.util.List;

public record SaleRequestDTO(
        String paymentMethod,
        List<SaleItemRequestDTO> items
) {
}
