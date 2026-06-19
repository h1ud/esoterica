package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.promotionDTO;
import com.webproject.esoteria.service.JwtService;
import com.webproject.esoteria.service.promotionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
public class AdminPromotionController {
    private final JwtService jwtService;
    private final promotionService promotionService;

    public AdminPromotionController(JwtService jwtService, promotionService promotionService) {
        this.jwtService = jwtService;
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<promotionDTO>> listPromotions(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // Poder admin: puede ver promociones activas e inactivas para gestionarlas.
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        return ResponseEntity.ok(promotionService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<promotionDTO> getPromotionById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        promotionDTO promotion = promotionService.getById(id);
        return promotion != null ? ResponseEntity.ok(promotion) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<promotionDTO> createPromotion(
            @Valid @RequestBody promotionDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<promotionDTO> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody promotionDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        promotionDTO updatedPromotion = promotionService.update(id, request);
        return updatedPromotion != null ? ResponseEntity.ok(updatedPromotion) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        promotionDTO deletedPromotion = promotionService.delete(id);
        return deletedPromotion != null ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private HttpStatus resolveAdminAccessError(String authorization) {
        // 401 = no se autentico con JWT valido. 403 = si tiene JWT, pero no tiene rol ADMIN.
        if (!jwtService.isValidAuthorizationHeader(authorization)) {
            return HttpStatus.UNAUTHORIZED;
        }

        return jwtService.isValidAdminAuthorizationHeader(authorization) ? null : HttpStatus.FORBIDDEN;
    }
}
