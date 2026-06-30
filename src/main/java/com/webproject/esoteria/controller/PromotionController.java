package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.PromotionDTO;
import com.webproject.esoteria.domain.dto.PromotionSaveDTO;
import com.webproject.esoteria.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
@CrossOrigin(origins = "http://localhost:4200") // Permite la comunicación con tu frontend Angular
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // 1. GET ALL: Listar todas las promociones
    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAll() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    // 2. GET BY ID: Buscar una promoción específica
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getById(@PathVariable long id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    // 3. POST: Crear una nueva promoción
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PromotionSaveDTO dto) {
        promotionService.savePromotion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 4. PUT: Actualizar una promoción existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody PromotionSaveDTO dto) {
        promotionService.updatePromotion(id, dto);
        return ResponseEntity.noContent().build();
    }

    // 5. DELETE: Eliminar una promoción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}