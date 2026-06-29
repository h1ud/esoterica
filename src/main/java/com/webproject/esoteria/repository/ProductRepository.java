package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
