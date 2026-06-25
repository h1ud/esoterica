package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.ProductDTO;
import com.webproject.esoteria.domain.dto.ProductSaveDTO;
import com.webproject.esoteria.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuAdminController {
    private final ProductService productService;

    public MenuAdminController(ProductService productService) {
        this.productService = productService;
    }

    // 5. LEER TODO O FILTRAR (GET) -> Alimenta la tabla y al Formulario Buscador
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(productService.getAllProducts(search));
    }

    // 6. CREAR PRODUCTO (POST) -> Conectado al Formulario "Agregar a la Carta"
    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@RequestBody ProductSaveDTO dto) {
        productService.saveProduct(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 7. ACTUALIZAR PRODUCTO (PUT) -> Conectado al Formulario "Editar Precios/Detalles"
    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductSaveDTO dto) {
        productService.updateProduct(id, dto);
        return ResponseEntity.noContent().build();
    }

    // 8. ELIMINAR PRODUCTO (DELETE) -> Botón de acción directa en la tabla
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
