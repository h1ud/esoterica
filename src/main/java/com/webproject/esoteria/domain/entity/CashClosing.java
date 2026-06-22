package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;

import java.math.BigDecimal;

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
@Setter
@Getter
@Entity
@Table(name = "cash_closing")
public class CashClosing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Username user;

    @Column(name = "emission_date", nullable = false)
    private LocalDateTime emissionDate;

    @Column(name = "total_efectivo", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalEfectivo;

    @Column(name = "total_yape_plin", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalYapePlin;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_last_sale_operation")
    private SaleOperation lastSaleOperation;

    @Column(length = 255)
    private String notes;

    public CashClosing() {
    }

    public CashClosing(Username user, BigDecimal totalEfectivo, BigDecimal totalYapePlin) {
        this.user = user;
        this.totalEfectivo = totalEfectivo;
        this.totalYapePlin = totalYapePlin;
        this.total = totalEfectivo.add(totalYapePlin);
        this.emissionDate = LocalDateTime.now();
    }
}
