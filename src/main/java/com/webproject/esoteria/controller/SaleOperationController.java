package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.SaleFinalyResponse;
import com.webproject.esoteria.domain.dto.SaleRequestDTO;
import com.webproject.esoteria.service.SaleOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pos/sales")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('COLABORADOR')")
public class SaleOperationController {

    private final SaleOperationService saleOperationService;

    public SaleOperationController(SaleOperationService saleOperationService) {
        this.saleOperationService = saleOperationService;
    }

    @PostMapping // Escucha peticiones POST en /api/sales
    public ResponseEntity<SaleFinalyResponse> createSale(@RequestBody SaleRequestDTO request) {
        // Llamamos al servicio y retornamos el resultado con estado 200 OK
        SaleFinalyResponse response = saleOperationService.executeSaleOperation(request);
        return ResponseEntity.ok(response);
    }
}