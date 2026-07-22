package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.CashClosingReportDTO;
import com.webproject.esoteria.domain.dto.ProductReportDTO;
import com.webproject.esoteria.domain.dto.SaleReportDTO;
import com.webproject.esoteria.repository.CashOpeningRepository;
import com.webproject.esoteria.repository.ProductRepository;
import com.webproject.esoteria.repository.SaleOperationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleReportService {

  private final SaleOperationRepository saleOperationRepository;
  private final ProductRepository productRepository;
  private final CashOpeningRepository cashOpeningRepository;

  public SaleReportService(
    SaleOperationRepository saleOperationRepository,
    ProductRepository productRepository,
    CashOpeningRepository cashOpeningRepository) {
    this.saleOperationRepository = saleOperationRepository;
    this.productRepository = productRepository;
    this.cashOpeningRepository = cashOpeningRepository;
  }

  public List<SaleReportDTO> generarReporteVentas(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

    return saleOperationRepository.obtenerReporteVentasPorFechas(startDateTime, endDateTime);
  }

  public List<ProductReportDTO> generarReporteProductos() {
    return productRepository.findAll().stream()
      .map(p -> new ProductReportDTO(
        p.getProductName(),
        p.getDescription(),
        p.getPrice(),
        p.getCategory() != null ? p.getCategory().getCategoryName() : "Sin categoría",
        p.isAvailable() ? "Disponible" : "No disponible"
      ))
      .toList();
  }

  // Reporte de cierre de caja usando CashOpening (tabla activa)
  public List<CashClosingReportDTO> generarReporteCierreCaja(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

    return cashOpeningRepository.obtenerReporteCierreCajaPorFechas(startDateTime, endDateTime);
  }
}
