package com.webproject.esoteria.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Username user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;

    @Column(name = "discount_type", nullable = false, length = 10)
    private String discountType;

    @Column(nullable = false, length = 10)
    private String visibility;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    public Promotion() {
    }

    public Promotion(Username user, String title, BigDecimal discount, String discountType) {
        this.user = user;
        this.title = title;
        this.discount = discount;
        this.discountType = discountType;
        this.visibility = "publica";
        this.isActive = true;
        this.startDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }
}
