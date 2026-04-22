package com.webproject.esoteria.domain.dto;
import lombok.Data;
@Data
public class productDTO {
    private Long id;
    private String product_name;
    private double price;
    private String activation;
    private String expiration;
}
