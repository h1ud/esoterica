package com.webproject.esoteria.service;

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

    // Constructor para la inyección de dependencias igual a tu ClientService
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 1. Lógica para Leer todos los productos (GET) - Con @Transactional(readOnly = true) por rendimiento
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

    // 2. Lógica para Buscar producto por ID (GET) - Mapeo inline directo sin método separado
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

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

    // 3. Lógica para Registrar Producto (POST)
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

    // 4. Lógica para Actualizar Producto (PUT) - Con .save() explícito al final
    // 1. Cambiamos ProductDTO por void
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

        // ❌ ¡Eliminamos el bloque 'return new ProductDTO(...)' por completo!
    }

    // 5. Lógica para Eliminar Producto (DELETE)
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("El producto no existe con ID: " + id);
        }
        productRepository.deleteById(id);
    }
}