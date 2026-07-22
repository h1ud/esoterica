package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.dto.SaleReportDTO;
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

  // ── Reporte de ventas ──────────────────────────────────────
  @Query("SELECT new com.webproject.esoteria.domain.dto.SaleReportDTO(" +
    "s.issueDate, " +
    "CONCAT(u.name, ' ', u.lastName), " +
    "s.paymentMethod, " +
    "s.paymentStatus, " +
    "s.subtotal, " +
    "s.discountAmount, " +
    "s.total) " +
    "FROM SaleOperation s JOIN s.user u " +
    "WHERE s.issueDate BETWEEN :startDate AND :endDate " +
    "ORDER BY s.issueDate DESC")
  List<SaleReportDTO> obtenerReporteVentasPorFechas(
    @Param("startDate") LocalDateTime startDate,
    @Param("endDate") LocalDateTime endDate
  );

  // ── Búsquedas por rango de fechas ──────────────────────────
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

  // ── Estadísticas diarias ──────────────────────────────────
  @Query("SELECT FUNCTION('date_trunc', 'day', s.issueDate) as day, " +
         "COUNT(s) as cnt, COALESCE(SUM(s.total), 0) as tot " +
         "FROM SaleOperation s WHERE s.issueDate >= :since " +
         "GROUP BY FUNCTION('date_trunc', 'day', s.issueDate) ORDER BY day ASC")
  List<Object[]> findDailyTotalsSince(@Param("since") LocalDateTime since);

  // ── Desglose por método de pago ────────────────────────────
  @Query("SELECT s.paymentMethod, COUNT(s), COALESCE(SUM(s.total), 0) " +
         "FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end GROUP BY s.paymentMethod")
  List<Object[]> findPaymentMethodBreakdown(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // ── Ventas por hora ─────────────────────────────────────────
  @Query("SELECT FUNCTION('date_part', 'hour', s.issueDate) as hr, " +
    "COUNT(s), COALESCE(SUM(s.total), 0) " +
    "FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end " +
    "GROUP BY FUNCTION('date_part', 'hour', s.issueDate) ORDER BY hr")
  List<Object[]> findHourlySalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // ── Ventas por turno (agrupado por hora) ────────────────────
  @Query("SELECT FUNCTION('date_part', 'hour', s.issueDate) as hr, " +
    "COUNT(s), COALESCE(SUM(s.total), 0) " +
    "FROM SaleOperation s WHERE s.issueDate BETWEEN :start AND :end " +
    "GROUP BY FUNCTION('date_part', 'hour', s.issueDate) ORDER BY hr")
  List<Object[]> findTurnSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // ── Descuentos aplicados ────────────────────────────────────
  @Query("SELECT s.id, CONCAT(u.name, ' ', u.lastName), s.subtotal, s.discountAmount, s.total, s.paymentMethod, s.issueDate " +
    "FROM SaleOperation s JOIN s.user u WHERE s.discountAmount > 0 " +
    "AND s.issueDate BETWEEN :start AND :end ORDER BY s.issueDate DESC")
  List<Object[]> findDiscountsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // ── Listados ordenados (derived queries) ───────────────────
  List<SaleOperation> findAllByOrderByIssueDateDesc();

  Page<SaleOperation> findAllByOrderByIssueDateDesc(Pageable pageable);
}
