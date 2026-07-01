package com.webproject.esoteria.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record UserDTO (
    Long id,
    String username,
    String name,
    String lastName,
    Long idRole,
    String roleName,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createDate
){
}
