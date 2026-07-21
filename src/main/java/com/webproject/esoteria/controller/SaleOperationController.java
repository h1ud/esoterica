package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.SaleFinalyResponse;
import com.webproject.esoteria.domain.dto.SaleRequestDTO;
import com.webproject.esoteria.domain.dto.SaleResponseDTO;
import com.webproject.esoteria.service.SaleOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pos/sales")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasAnyRole('COLABORADOR', 'ADMIN', 'BACKOFFICE')")
public class SaleOperationController {

    private final SaleOperationService saleOperationService;

    public SaleOperationController(SaleOperationService saleOperationService) {
        this.saleOperationService = saleOperationService;
    }

    @PostMapping
    public ResponseEntity<SaleFinalyResponse> createSale(@RequestBody SaleRequestDTO request) {
        SaleFinalyResponse response = saleOperationService.executeSaleOperation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SaleResponseDTO>> getRecentSales() {
        List<SaleResponseDTO> sales = saleOperationService.getRecentSales();
        return ResponseEntity.ok(sales);
    }
}