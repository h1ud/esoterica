package com.webproject.esoteria.domain.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SaleOperation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private Date issue_date;

    //fk from User.java
}
