package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    java.util.Optional<Promotion> findByCode(String code);
}