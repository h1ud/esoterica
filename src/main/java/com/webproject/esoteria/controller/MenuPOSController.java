package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.CategoryDTO;
import com.webproject.esoteria.domain.dto.ProductDTO;
import com.webproject.esoteria.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pos/menu")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasAnyRole('ADMIN', 'BACKOFFICE', 'COLABORADOR')")
public class MenuPOSController {

    private final ProductService productService;

    public MenuPOSController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
}