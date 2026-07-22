package com.webproject.esoteria.domain.dto;

import java.math.BigDecimal;

public record ProductReportDTO(
  String productName,
  String description,
  BigDecimal price,
  String categoryName,          // <-- Aquí viaja el nombre (ej. "Bebidas", "Entradas")
  String availabilityStatus     // "Disponible" o "No disponible"
) {}
