package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.ProductDTO;
import com.webproject.esoteria.domain.dto.ProductSaveDTO;
import com.webproject.esoteria.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuAdminController {
    private final ProductService productService;

    // Inyectamos directamente tu clase ProductService
    public MenuAdminController(ProductService productService) {
        this.productService = productService;
    }

    // GET: /api/products -> Trae todos los productos de golpe
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // POST: /api/products -> Crea un nuevo producto mapeado a su categoría
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    // PUT: /api/products/{id} -> Actualiza un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductSaveDTO dto) {
        productService.updateProduct(id, dto);
        return ResponseEntity.noContent().build(); // Devuelve estado 204 sin cuerpo
    }

    // DELETE: /api/products/{id} -> Borra un producto del menú
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
