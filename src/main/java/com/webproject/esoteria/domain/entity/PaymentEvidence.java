package com.webproject.esoteria.domain.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment_evidence")
public class PaymentEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "id_sale_operation", nullable = false, unique = true)
    private SaleOperation saleOperation;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public PaymentEvidence() {
    }

    public PaymentEvidence(SaleOperation saleOperation, String imageUrl) {
        this.saleOperation = saleOperation;
        this.imageUrl = imageUrl;
        this.uploadedAt = LocalDateTime.now();
    }
}
