package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/products")
public class productController {
    @Autowired
    private productService productService;

     @PostMapping
    public ResponseEntity<productDTO> guardar(@RequestBody productDTO productDTO) {
         productDTO nuevoProducto = productService.crearProduct(productDTO);
         return ResponseEntity.ok(nuevoProducto);
     }
     @GetMapping
    public ResponseEntity<List<Product>> buscarProducto(Long id) {
         return ResponseEntity.ok(productService.listarTodo());
     }
}
