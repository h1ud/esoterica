package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Username {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String username;
    private String password_hash;
    private String first_name;
    private String last_name;

    private LocalDateTime create_date;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @OneToMany(mappedBy = "username")
    private List<CloseSession> closeSessions;

    @OneToMany(mappedBy = "username")
    private List<SaleOperation> saleOperations;


    

}
