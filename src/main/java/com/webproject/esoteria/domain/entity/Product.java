package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String productName;
    private double price;
    private LocalDateTime activation;
    private LocalDateTime expiration;

    public Product(String productName, double price) {
        this.productName = productName;
        this.price = price;
        this.activation = LocalDateTime.now();
        this.expiration = LocalDateTime.now().plusYears(1); 
    }

}
