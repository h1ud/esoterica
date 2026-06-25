package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientDTO(
        Long id,
        String name,
        String dni,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthdayDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createDate
) {

}
