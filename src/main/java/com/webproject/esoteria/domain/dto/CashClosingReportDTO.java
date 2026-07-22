package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashClosingReportDTO(
  LocalDateTime emissionDate,
  String colaborador,
  BigDecimal totalEfectivo,
  BigDecimal totalYapePlin,
  BigDecimal totalTarjeta,
  BigDecimal total,
  String notes
) {}
