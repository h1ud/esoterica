package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.productDTO;

import com.webproject.esoteria.domain.mapper.productMapper;
import com.webproject.esoteria.repository.productRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.annotation.PostConstruct;

@Service
public class productService {

    private final Map<Long, productDTO> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    private void init() {
        productDTO p1 = new productDTO();
        p1.setProduct_name("Galletas");
        p1.setPrice(2.5);
        p1.setActivation("2026-01-01");
        p1.setExpiration("2027-01-01");
        make(p1);

        productDTO p2 = new productDTO();
        p2.setProduct_name("Jugo");
        p2.setPrice(1.75);
        p2.setActivation("2026-02-01");
        p2.setExpiration("2027-02-01");
        make(p2);
    }

    public productDTO make(productDTO dto) {
        long id = idGenerator.getAndIncrement();
        // guardar copia para evitar aliasing
        productDTO copy = copyDto(dto);
        store.put(id, copy);
        return copyDto(copy);
    }

    public List<productDTO> listAll() {
        return store.values().stream().map(this::copyDto).toList();
    }

    public productDTO search(Long id) {
        return copyDto(store.get(id));
    }

    public productDTO delete(Long id) {
        return copyDto(store.remove(id));
    }

    public productDTO update(Long id, productDTO dto) {
        return store.computeIfPresent(id, (k, existing) -> {
            existing.setProduct_name(dto.getProduct_name() != null ? dto.getProduct_name() : existing.getProduct_name());
            existing.setPrice(dto.getPrice() != 0.0 ? dto.getPrice() : existing.getPrice());
            existing.setActivation(dto.getActivation() != null ? dto.getActivation() : existing.getActivation());
            existing.setExpiration(dto.getExpiration() != null ? dto.getExpiration() : existing.getExpiration());
            return existing;
        }) != null ? copyDto(store.get(id)) : null;
    }

    public List<productDTO> searchByName(String name) {
        String lower = name == null ? "" : name.toLowerCase();
        return store.values().stream()
                .filter(p -> p.getProduct_name() != null && p.getProduct_name().toLowerCase().contains(lower))
                .map(this::copyDto)
                .toList();
    }

<<<<<<< HEAD
    private productDTO copyDto(productDTO src) {
        if (src == null) return null;
        productDTO c = new productDTO();
        c.setProduct_name(src.getProduct_name());
        c.setPrice(src.getPrice());
        c.setActivation(src.getActivation());
        c.setExpiration(src.getExpiration());
        return c;
=======
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
>>>>>>> 49a7db7 (role-implements)
    }
}
