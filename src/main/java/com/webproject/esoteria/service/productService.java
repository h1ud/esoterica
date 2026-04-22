package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.repository.productRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class productService {
    @Autowired
    private productRepository productRepository;
    @Autowired
    private productMapper productMapper;

    public Product buscarProducto(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product crearProducto(Product product) {
        return productRepository.save(product);
    }
    public Product eliminarProducto(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
        }
        return product;
    }
    public Product actualizarProducto(Long id, productDTO productDto) {
        return productRepository.findById(id).map(existingProduct -> {
            productMapper.updateProductFromDto(productDto, existingProduct);
            return productRepository.save(existingProduct);
        }).orElse(null);
    }
    public productDTO crearProduct(productDTO productDTO){
        Product productToSave = productMapper.toEntity(productDTO);
        Product productSave = productRepository.save(productToSave);
        return productMapper.toDto(productSave);

    }
        public List<Product> listarTodo() {
            return productRepository.findAll();
        }
}
