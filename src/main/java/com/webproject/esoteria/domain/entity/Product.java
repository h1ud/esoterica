package com.webproject.esoteria.domain.entity;

import java.math.BigDecimal;

import java.math.BigDecimal;
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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    public Product() {
    }

    public Product(Category category, String productName, BigDecimal price) {
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.isAvailable = true;
    }

}
