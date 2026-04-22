package com.webproject.esoteria.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class SaleDetail {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private int quantity;
    private double price;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="sale_operation_id")
    private SaleOperation saleOperation;


}
