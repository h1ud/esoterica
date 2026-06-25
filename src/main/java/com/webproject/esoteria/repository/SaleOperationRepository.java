package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.SaleOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOperationRepository extends JpaRepository<SaleOperation, Long> {
}
