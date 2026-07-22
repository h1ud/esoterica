package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.dto.CashClosingReportDTO;
import com.webproject.esoteria.domain.entity.CashClosing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashClosingRepository extends JpaRepository<CashClosing, Long> {
  @Query("SELECT new com.webproject.esoteria.domain.dto.CashClosingReportDTO(" +
    "c.emissionDate, " +
    "CONCAT(u.name, ' ', u.lastName), " + // Ajusta según los campos de nombres de tu entidad User
    "c.totalEfectivo, " +
    "c.totalYapePlin, " +
    "c.totalTarjeta, " +
    "c.total, " +
    "c.notes) " +
    "FROM CashClosing c JOIN c.user u " +
    "WHERE c.emissionDate BETWEEN :startDate AND :endDate")
  List<CashClosingReportDTO> obtenerReporteCierreCajaPorFechas(
    @Param("startDate") LocalDateTime startDate,
    @Param("endDate") LocalDateTime endDate
  );
}
