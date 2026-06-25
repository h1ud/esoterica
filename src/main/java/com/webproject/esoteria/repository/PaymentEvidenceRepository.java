package com.webproject.esoteria.repository;
import com.webproject.esoteria.domain.entity.PaymentEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentEvidenceRepository extends JpaRepository<PaymentEvidence, Long> {
}
