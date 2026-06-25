package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionDTO(
        Long id,
        String title,
        String description,
        BigDecimal discount,
        String discountType,
        boolean isActive,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        String creatorUsername
) {
}
