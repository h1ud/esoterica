package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class clientDTO {

    private Long id;
    private String name;
    private String password_hash;
    private String dni;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday_date;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime create_date;

    public clientDTO() {
    }

    public clientDTO(Long id, String name, String password_hash, String dni, LocalDate birthday_date, LocalDateTime create_date) {
        this.id = id;
        this.name = name;
        this.password_hash = password_hash;
        this.dni = dni;
        this.birthday_date = birthday_date;
        this.create_date = create_date;
    }
}

