package com.webproject.esoteria.repository;

import com.webproject.esoteria.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface productRepository extends JpaRepository<Product,Long> {
}
