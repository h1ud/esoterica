package com.webproject.esoteria.domain.dto;

import java.util.List;

public record SaleRequestDTO(
        String paymentMethod,
        List<SaleItemRequestDTO> items,
        String promoCode,
        String documentType,
        String clientDni,
        String clientName,
        String clientBusinessName,
        String clientAddress
) {
}
