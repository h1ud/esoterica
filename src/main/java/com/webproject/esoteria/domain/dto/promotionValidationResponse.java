package com.webproject.esoteria.domain.dto;

import lombok.Data;

@Data
public class promotionValidationResponse {
    private boolean valid;
    private String message;
    private String zodiac_sign;
    private String selected_topping;
    private Double total;
    private promotionDTO promotion;
}
