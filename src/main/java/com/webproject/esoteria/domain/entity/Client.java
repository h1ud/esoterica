package com.webproject.esoteria.domain.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Client {
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String clientName;
    private String password_hash;
    private String dni;
    private LocalDate birthday_date;
    
    @OneToMany(mappedBy="client")
    private List<DiscountsCode> discounts_codes;

    public Client(String clientName, String password_hash, String dni){
        this.clientName = clientName;
        this.password_hash = password_hash;
        this.dni=dni;
        this.birthday_date=LocalDate.now();
    }
}
