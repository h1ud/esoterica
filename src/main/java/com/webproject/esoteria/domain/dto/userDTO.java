package com.webproject.esoteria.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class userDTO {


    private String username;
    private String password_hash;
    private String first_name;
    private String last_name;

    private Date create_date;
}
