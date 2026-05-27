package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.promotionDTO;
import com.webproject.esoteria.domain.dto.promotionQuoteRequest;
import com.webproject.esoteria.domain.dto.promotionValidationResponse;
import com.webproject.esoteria.service.promotionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class promotionController {
    private final promotionService promotionService;

    public promotionController(promotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<promotionDTO>> listAll() {
        // Publico: los clientes solo ven promociones activas; el admin puede ver tambien inactivas.
        return ResponseEntity.ok(promotionService.listActive());
    }

    @GetMapping("/today")
    public ResponseEntity<List<promotionDTO>> listToday(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(promotionService.listForDate(date));
    }

    @GetMapping("/validate")
    public ResponseEntity<promotionValidationResponse> validate(
            @RequestParam String sign,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(promotionService.validate(sign, date));
    }

    @PostMapping("/quote")
    public ResponseEntity<promotionValidationResponse> quote(@RequestBody promotionQuoteRequest request) {
        return ResponseEntity.ok(promotionService.quote(request));
    }
}
