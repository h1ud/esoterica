package com.webproject.esoteria.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity 
public class Role {

    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String role_name;

}
