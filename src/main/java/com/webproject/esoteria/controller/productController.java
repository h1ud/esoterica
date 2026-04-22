package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.productDTO;
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
    public ResponseEntity<productDTO> makeProduct(@RequestBody productDTO productDTO) {
         productDTO nuevoProducto = productService.make(productDTO);
         return ResponseEntity.ok(nuevoProducto);
     }
     @GetMapping
    public ResponseEntity<List<productDTO>> listProduct() {
         return ResponseEntity.ok(productService.listAll());
     }
     @GetMapping("/{id}")
     public ResponseEntity<productDTO> searchProduct(@PathVariable Long id) {
         productDTO product = productService.search(id);
         return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
     }
    @DeleteMapping("/{id}")
     public ResponseEntity<productDTO> deleteProduct(@PathVariable Long id) {
         productDTO productoEliminado = productService.delete(id);
         return productoEliminado != null ? ResponseEntity.ok(productoEliminado) : ResponseEntity.notFound().build();
     }
    @PutMapping("/{id}")
    public ResponseEntity<productDTO> updateProduct(@PathVariable Long id, @RequestBody productDTO dto){
        productDTO updatedProduct = productService.update(id, dto);
        return updatedProduct != null
                ? ResponseEntity.ok(updatedProduct)
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<productDTO>> searchProductByName(@RequestParam String name){
         return ResponseEntity.ok(productService.searchByName(name));
    }

}
