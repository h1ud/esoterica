package com.webproject.esoteria.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String dayOfWeek;
    private String zodiacSigns;
    private String description;
    private double promoPrice;
    private int requiredCrepes;
    private int requiredFruits;
    private int requiredToppings;
    private String toppingOptions;
    private Boolean active;
}
