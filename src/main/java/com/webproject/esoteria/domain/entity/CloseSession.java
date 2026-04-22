package com.webproject.esoteria.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class CloseSession {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private LocalDate emissionDate;
    private double total;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Username username;
}
