package com.webproject.esoteria.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.domain.entity.Username;

@Data
public class userDTO {


private String username;
    private String password_hash;
    private String first_name;
    private String last_name;
    private Role role;  // 
    private LocalDateTime create_date;

    public userDTO() {
    }

    public userDTO(Username usernameEntity) {
        this.username = usernameEntity.getUsername();
        this.password_hash = usernameEntity.getPassword_hash();
        this.first_name = usernameEntity.getFirst_name();
        this.last_name = usernameEntity.getLast_name();
        this.role = usernameEntity.getRole();
        this.create_date = usernameEntity.getCreate_date();
    }
}
