package com.webproject.esoteria.domain.entity;

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
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_promotion", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    public PromoCode() {
    }

    public PromoCode(Promotion promotion, Client client, String code) {
        this.promotion = promotion;
        this.client = client;
        this.code = code;
        this.isActive = true;
        this.isUsed = false;
        this.createdAt = LocalDateTime.now();
    }
}
