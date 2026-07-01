package com.webproject.esoteria.domain.dto;

import com.webproject.esoteria.domain.entity.Visibility;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionSaveDTO(
        long userId,
        String title,
        String description,
        BigDecimal discount,
        Visibility visibility,
        boolean isActive,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String imageUrl
) {}
