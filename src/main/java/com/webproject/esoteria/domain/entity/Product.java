package com.webproject.esoteria.domain.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String product_name;
    private double price;
    private Date activation;
    private Date expiration;
}
