package com.webproject.esoteria.domain.entity;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class SaleOperation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private Date issue_date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Username username;

    @OneToMany(mappedBy = "saleOperation")
    private List<SaleDetail> saleDetails;
    
}
