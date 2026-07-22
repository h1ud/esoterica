package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

  @Query("SELECT cat.categoryName, SUM(d.quantity), COALESCE(SUM(d.subtotal), 0) " +
    "FROM SaleDetail d JOIN d.product p JOIN p.category cat " +
    "JOIN d.saleOperation s WHERE s.issueDate BETWEEN :start AND :end " +
    "GROUP BY cat.categoryName ORDER BY SUM(d.subtotal) DESC")
  List<Object[]> findCategorySalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT p.productName, cat.categoryName, SUM(d.quantity), COALESCE(SUM(d.subtotal), 0) " +
    "FROM SaleDetail d JOIN d.product p JOIN p.category cat " +
    "JOIN d.saleOperation s WHERE s.issueDate BETWEEN :start AND :end " +
    "GROUP BY p.productName, cat.categoryName ORDER BY SUM(d.subtotal) DESC")
  List<Object[]> findTopProductsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT p.productName, cat.categoryName, SUM(d.quantity), COALESCE(SUM(d.subtotal), 0) " +
    "FROM SaleDetail d JOIN d.product p JOIN p.category cat " +
    "JOIN d.saleOperation s WHERE s.issueDate BETWEEN :start AND :end " +
    "GROUP BY p.productName, cat.categoryName ORDER BY SUM(d.subtotal) ASC")
  List<Object[]> findBottomProductsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}