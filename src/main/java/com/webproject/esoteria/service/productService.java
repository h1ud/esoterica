package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.repository.productRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class productService {
    private final productRepository productRepository;
    private final productMapper productMapper;

    public productService(productRepository productRepository, productMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public productDTO make(productDTO dto) {
        Product product = productMapper.toEntity(dto);
        product.setActivation(product.getActivation() != null ? product.getActivation() : LocalDateTime.now());
        product.setExpiration(product.getExpiration() != null ? product.getExpiration() : LocalDateTime.now().plusYears(1));

        return productMapper.toDto(productRepository.save(product));
    }

    public List<productDTO> listAll() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    public productDTO search(Long id) {
        return productRepository.findById(id).map(productMapper::toDto).orElse(null);
    }

    public productDTO delete(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productDTO deleted = productMapper.toDto(product);
                    productRepository.delete(product);
                    return deleted;
                })
                .orElse(null);
    }

    public productDTO update(Long id, productDTO dto) {
        return productRepository.findById(id)
                .map(existing -> {
                    productMapper.updateProductFromDto(dto, existing);
                    return productMapper.toDto(productRepository.save(existing));
                })
                .orElse(null);
    }

    public List<productDTO> searchByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name == null ? "" : name)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}
