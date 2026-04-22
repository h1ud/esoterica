package com.webproject.esoteria.domain.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity 
public class Role {

    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String role_name;

    @OneToMany(mappedBy = "role")
    private List<Username> usernames;

    public Role (String role_name){
        this.role_name = role_name;
    }
}
