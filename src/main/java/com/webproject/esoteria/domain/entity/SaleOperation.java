package com.webproject.esoteria.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_promo_code")
    private PromoCode promoCode;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 15)
    private String paymentStatus;

    @Column(name = "document_type", length = 15)
    private String documentType;

    @Column(name = "client_dni", length = 15)
    private String clientDni;

    @Column(name = "client_name", length = 150)
    private String clientName;

    @Column(name = "client_business_name", length = 200)
    private String clientBusinessName;

    @Column(name = "client_address", length = 255)
    private String clientAddress;

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

    @OneToMany(mappedBy = "saleOperation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetail> details = new ArrayList<>();

    public SaleOperation() {
    }

    public SaleOperation(User user, String paymentMethod) {
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "pendiente";
        this.subtotal = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.issueDate = LocalDateTime.now();
    }
}
