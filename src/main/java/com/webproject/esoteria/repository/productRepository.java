package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface productRepository extends JpaRepository<Product,Long> {
    List<Product> findByProductNameContainingIgnoreCase(String product_name);
    List<Product> findByPriceBetween(double min, double max);
}
