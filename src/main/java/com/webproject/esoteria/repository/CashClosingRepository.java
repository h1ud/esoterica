package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.CashClosing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashClosingRepository extends JpaRepository<CashClosing, Long> {
}