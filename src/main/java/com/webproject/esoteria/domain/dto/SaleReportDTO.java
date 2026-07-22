package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleReportDTO(
  LocalDateTime issueDate,
  String colaborador,
  String paymentMethod,
  String paymentStatus,
  BigDecimal subtotal,
  BigDecimal discountAmount,
  BigDecimal total
) {}
