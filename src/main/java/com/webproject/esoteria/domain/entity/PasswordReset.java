package com.webproject.esoteria.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "password_reset") // <-- Nombre de la tabla simplificado
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructor vacío a mano para Hibernate
    public PasswordReset() {
    }

    public PasswordReset(String name, String lastName, String username, String email) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

}