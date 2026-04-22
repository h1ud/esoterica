package com.webproject.esoteria.domain.dto;

import lombok.Data;

@Data
public class roleDTO {
    private Long id;
    private String role_name;

    public roleDTO(Long id, String role_name) {
        this.id = id;
        this.role_name = role_name;
    }
}

