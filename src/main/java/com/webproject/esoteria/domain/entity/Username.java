package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class Username {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public Username() {
    }

    public Username(Role role, String username, String passwordHash, String name, String lastName) {
        this.role = role;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.lastName = lastName;
        this.createDate = LocalDateTime.now();
    }
}
