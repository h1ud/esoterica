package com.webproject.esoteria.domain.dto;

public record EmployeeSaveDTO(
        String username,
        String password,
        String name,
        String lastName,
        Long roleId
) {
}
