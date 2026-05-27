package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface promotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByActiveTrue();
    List<Promotion> findByActiveTrueAndDayOfWeek(String dayOfWeek);
}
