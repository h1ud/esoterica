package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleFinalyResponse(
        Long saleOperationId,
        String paymentStatus,
        BigDecimal totalAmount,
        LocalDateTime issueDate
) {
}