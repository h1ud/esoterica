package com.webproject.esoteria.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "client")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(nullable = false, unique = true, length = 15)
	private String dni;

	@Column(name = "birthday_date")
	private LocalDate birthdayDate;

	@Column(name = "create_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createDate;

	@OneToMany(mappedBy = "client")
	private List<PromoCode> discountsCodes;

	public Client() {
	}

	public Client(String name, String password_hash, String dni) {
		this.name = name;
		this.passwordHash = password_hash;
		this.dni = dni;
		this.birthdayDate = LocalDate.now();
		this.createDate = LocalDateTime.now();
	}
}
