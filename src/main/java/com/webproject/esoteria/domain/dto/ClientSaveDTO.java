package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ClientSaveDTO(
        String name,
        @JsonProperty("password_hash") // Mapea password_hash del JSON a esta variable
        String password,
        String dni,

        @JsonProperty("birthdayDate") // Mapea birthday_date del JSON a esta variable
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthdayDate
) {
}
