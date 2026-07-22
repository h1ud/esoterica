package com.webproject.esoteria.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Visibility visibility;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "create_date", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createDate;


    public Promotion() {
    }

    public Promotion(User user, String code, String title, BigDecimal discount) {
        this.user = user;
        this.code = code;
        this.title = title;
        this.discount = discount;
        this.visibility = Visibility.GLOBAL;
        this.isActive = true;
        this.startDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }
}
