package com.webproject.esoteria.service;


import com.webproject.esoteria.domain.dto.ProductDTO;
import com.webproject.esoteria.domain.dto.ProductSaveDTO;
import com.webproject.esoteria.domain.entity.Category;
import com.webproject.esoteria.domain.entity.Product;
import com.webproject.esoteria.repository.CategoryRepository;
import com.webproject.esoteria.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 5. Lógica para Leer o Filtrar Productos (GET) -> Para el Formulario de Buscador
    public List<ProductDTO> getAllProducts(String search) {
        List<Product> products;

        // Si Angular envía un término de búsqueda, filtramos; si no, traemos todo
        if (search != null && !search.trim().isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(search);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.isAvailable(),
                        p.getCategory() != null ? p.getCategory().getId() : null,
                        p.getCategory() != null ? p.getCategory().getCategoryName() : "sin Categoría"
                )).toList();
    }

    // 6. Lógica para Crear Producto (POST)
    public void saveProduct(ProductSaveDTO dto) {
        Product product = new Product();
        product.setProductName(dto.productName());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setAvailable(true);

        // Buscamos la categoría en la BD usando el ID enviado desde Angular
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.categoryId()));
        product.setCategory(category);

        productRepository.save(product);
    }

    // 7. Lógica para Editar Producto (PUT)
    public void updateProduct(Long id, ProductSaveDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        product.setProductName(dto.productName());
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        product.setCategory(category);

        productRepository.save(product);
    }

    // 8. Lógica para Eliminar Producto (DELETE)
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
