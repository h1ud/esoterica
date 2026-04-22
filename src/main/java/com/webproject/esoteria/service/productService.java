package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.repository.productRepository;
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
}
