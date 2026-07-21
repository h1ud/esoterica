package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.SaleOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleOperationRepository extends JpaRepository<SaleOperation, Long> {
    List<SaleOperation> findAllByOrderByIssueDateDesc();

    Page<SaleOperation> findAllByOrderByIssueDateDesc(Pageable pageable);

    @Query("SELECT s FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end ORDER BY s.issueDate ASC")
    List<SaleOperation> findByIssueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end")
    long countByIssueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end")
    BigDecimal sumTotalByIssueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end AND s.paymentMethod = 'efectivo'")
    BigDecimal sumEfectivoByIssueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end AND s.paymentMethod = 'yape_plin'")
    BigDecimal sumYapePlinByIssueDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT FUNCTION('date_trunc', 'day', s.issueDate) as day, COUNT(s) as cnt, COALESCE(SUM(s.total), 0) as tot " +
           "FROM SaleOperation s WHERE s.issueDate >= :since GROUP BY FUNCTION('date_trunc', 'day', s.issueDate) ORDER BY day ASC")
    List<Object[]> findDailyTotalsSince(@Param("since") LocalDateTime since);

    @Query("SELECT s.paymentMethod, COUNT(s), COALESCE(SUM(s.total), 0) " +
           "FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end GROUP BY s.paymentMethod")
    List<Object[]> findPaymentMethodBreakdown(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM SaleOperation s WHERE s.issueDate >= :since")
    long countSince(@Param("since") LocalDateTime since);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM SaleOperation s WHERE s.issueDate >= :since")
    BigDecimal sumTotalSince(@Param("since") LocalDateTime since);
}
