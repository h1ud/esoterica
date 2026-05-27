package com.webproject.esoteria.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class promotionDTO {
    private Long id;
    private String name;
    private String day_of_week;
    private List<String> zodiac_signs;
    private String description;
    private Double promo_price;
    private Integer required_crepes;
    private Integer required_fruits;
    private Integer required_toppings;
    private List<String> topping_options;
    private Boolean active;
}
