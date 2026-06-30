package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ClientSaveDTO(
        String name,
        @JsonProperty("password_hash")
        String password,
        String dni,

        @JsonProperty("birthdayDate")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthdayDate
) {
}
