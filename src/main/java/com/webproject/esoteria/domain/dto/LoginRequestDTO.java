package com.webproject.esoteria.domain.dto;

// DTO de entrada: credenciales que llegan desde frontend o Postman para iniciar sesion.
public class LoginRequestDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
