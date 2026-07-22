package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.CashClosingReportDTO;
import com.webproject.esoteria.domain.dto.ProductReportDTO;
import com.webproject.esoteria.domain.dto.SaleReportDTO;
import com.webproject.esoteria.service.SaleReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class ReportAdminController {
  private final SaleReportService reportService;

  public ReportAdminController(SaleReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping
  public ResponseEntity<?> getReport(
    @RequestParam String type,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    switch (type.toLowerCase()) {
      case "sales":
        List<SaleReportDTO> salesReport = reportService.generarReporteVentas(startDate, endDate);
        return ResponseEntity.ok(salesReport);

      case "products":
        List<ProductReportDTO> productsReport = reportService.generarReporteProductos();
        return ResponseEntity.ok(productsReport);

      case "cash":
        List<CashClosingReportDTO> cashReport = reportService.generarReporteCierreCaja(startDate, endDate);
        return ResponseEntity.ok(cashReport);

      default:
        return ResponseEntity.badRequest().body("Tipo de reporte no válido");
    }
  }
}
