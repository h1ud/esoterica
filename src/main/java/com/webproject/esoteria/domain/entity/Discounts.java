package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class DiscountsCode {

    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String codeName;
    private int codeValue;
    private Boolean isActivate;
    private Boolean isUse;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    public DiscountsCode(String codeName, int codeValue, Boolean isActivate, Boolean isUse){
        this.codeName=codeName;
        this.codeValue=codeValue;
        this.isActivate=isActivate;
        this.isUse=isUse;
        this.createdAt=LocalDateTime.now();
    }
}
