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
@Table(name = "sale_operation")
public class SaleOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private Username user;

    @ManyToOne
    @JoinColumn(name = "id_promo_code")
    private PromoCode promoCode;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 15)
    private String paymentStatus;

    @Column(length = 255)
    private String notes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    public SaleOperation() {
    }

    public SaleOperation(Username user, String paymentMethod) {
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "pendiente";
        this.subtotal = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.issueDate = LocalDateTime.now();
    }
}
