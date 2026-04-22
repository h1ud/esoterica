package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.repository.productRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class productService {
    @Autowired
    private productRepository productRepository;
    @Autowired
    private productMapper productMapper;

    public productDTO search(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElse(null);
    }
    public productDTO make(productDTO dto) {
        Product entity = productMapper.toEntity(dto);
        Product saved = productRepository.save(entity);
        return productMapper.toDto(saved);
    }
    public productDTO delete(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return productMapper.toDto(product);
                }).orElse(null);
    }
    public productDTO update(Long id, productDTO productDto) {
        return productRepository.findById(id)
                .map(existingProduct -> {
            productMapper.updateProductFromDto(productDto, existingProduct);
            Product update = productRepository.save(existingProduct);
            return productMapper.toDto(update);
        }).orElse(null);
    }

        public List<productDTO> listAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
        }

        public List<productDTO> searchByName(String name) {
            return productRepository.findByProductNameContainingIgnoreCase(name)
                    .stream()
                    .map(productMapper::toDto)
                    .collect(toList());
        }

        public List<productDTO> findByProceRange(double min, double max) {
            return productRepository.findByPriceBetween(min, max)
                    .stream()
                    .map(productMapper::toDto)
                    .collect(toList());
        }
        // Método para agregar productos de ejemplo al iniciar la aplicación
    @PostConstruct
    public void init() {
        // Solo agrega productos si la base de datos está vacía
        if (productRepository.count() == 0) {
            productRepository.save(new Product("Producto A", 100.0));
            productRepository.save(new Product("Producto B", 150.0));
            productRepository.save(new Product("Producto C", 200.0));
        }
    }
}
