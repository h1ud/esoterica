package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.CategoryDTO;
import com.webproject.esoteria.domain.dto.ProductDTO;
import com.webproject.esoteria.domain.dto.ProductSaveDTO;
import com.webproject.esoteria.domain.entity.Category;
import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.repository.ProductRepository;
import com.webproject.esoteria.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {


    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.isAvailable(),
                        p.getCategory().getId(),
                        p.getCategory().getCategoryName()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("producto no encontrado con ID: " + id));

        return new ProductDTO(
                p.getId(),
                p.getProductName(),
                p.getDescription(),
                p.getPrice(),
                p.isAvailable(),
                p.getCategory().getId(),
                p.getCategory().getCategoryName()
        );
    }

    @Transactional
    public ProductDTO createProduct(ProductSaveDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría inmutable no encontrada: " + dto.categoryId()));

        Product product = new Product(category, dto.productName(), dto.price());
        product.setDescription(dto.description());

        Product savedProduct = productRepository.save(product);

        return new ProductDTO(
                savedProduct.getId(),
                savedProduct.getProductName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.isAvailable(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getCategoryName()
        );
    }

    @Transactional
    public void updateProduct(Long id, ProductSaveDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.categoryId()));

        product.setProductName(dto.productName());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setCategory(category);

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("El producto no existe con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_IdAndIsAvailableTrue(categoryId).stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.isAvailable(),
                        p.getCategory().getId(),
                        p.getCategory().getCategoryName()
                ))
                .toList();
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDTO(
                        c.getId(),
                        c.getCategoryName(),
                        c.getDescription()
                ))
                .toList();
    }
}