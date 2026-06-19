package com.webproject.esoteria.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Client {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String name;
	private String password_hash;
	private String dni;
	private LocalDate birthday_date;
	private LocalDateTime create_date;

	@OneToMany(mappedBy="client")
	private List<DiscountsCode> discounts_codes;

	public Client() {
	}

	public Client(String name, String password_hash, String dni){
		this.name = name;
		this.password_hash = password_hash;
		this.dni=dni;
		this.birthday_date=LocalDate.now();
		this.create_date = LocalDateTime.now();
	}
}
