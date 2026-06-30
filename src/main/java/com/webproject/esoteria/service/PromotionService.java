package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.PromotionDTO;
import com.webproject.esoteria.domain.dto.PromotionSaveDTO;
import com.webproject.esoteria.domain.entity.Promotion;
import com.webproject.esoteria.domain.entity.User;
import com.webproject.esoteria.repository.PromotionRepository;
import com.webproject.esoteria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. LISTAR TODAS LAS PROMOCIONES (GET)
    @Transactional(readOnly = true)
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(p -> new PromotionDTO(
                        p.getId(),
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getDiscount(),
                        p.getVisibility(),
                        p.isActive(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getImageUrl(),
                        p.getCreateDate()
                ))
                .toList();
    }

    // 2. BUSCAR UNA PROMOCIÓN POR ID (GET)
    @Transactional(readOnly = true)
    public PromotionDTO getPromotionById(long id) {
        Promotion p = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        return new PromotionDTO(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getUsername(),
                p.getTitle(),
                p.getDescription(),
                p.getDiscount(),
                p.getVisibility(),
                p.isActive(),
                p.getStartDate(),
                p.getEndDate(),
                p.getImageUrl(),
                p.getCreateDate()
        );
    }

    // 3. GUARDAR UNA NUEVA PROMOCIÓN (POST)
    @Transactional
    public void savePromotion(PromotionSaveDTO dto) {
        // 1. Extraemos el username (ej: "u23239739") directamente del Token JWT autenticado
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscamos al usuario real por su username en lugar de usar el ID del DTO
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado con username: " + currentUsername));

        Promotion p = new Promotion();
        p.setUser(user); // 👈 Ahora sí quedará asociado al usuario real
        p.setTitle(dto.title());
        p.setDescription(dto.description());
        p.setDiscount(dto.discount());
        p.setVisibility(dto.visibility());
        p.setActive(dto.isActive());
        p.setStartDate(dto.startDate());
        p.setEndDate(dto.endDate());
        p.setImageUrl(dto.imageUrl());

        promotionRepository.save(p);
    }

    // 4. ACTUALIZAR UNA PROMOCIÓN (PUT)
    @Transactional
    public void updatePromotion(long id, PromotionSaveDTO dto) {
        Promotion p = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        // Hacemos lo mismo para el Update, así garantizamos consistencia si re-guarda el creador
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado con username: " + currentUsername));

        p.setUser(user);
        p.setTitle(dto.title());
        p.setDescription(dto.description());
        p.setDiscount(dto.discount());
        p.setVisibility(dto.visibility());
        p.setActive(dto.isActive());
        p.setStartDate(dto.startDate());
        p.setEndDate(dto.endDate());
        p.setImageUrl(dto.imageUrl());

        promotionRepository.save(p);
    }

    // 5. ELIMINAR UNA PROMOCIÓN (DELETE)
    @Transactional
    public void deletePromotion(long id) {
        if (!promotionRepository.existsById(id)) {
            throw new RuntimeException("La promoción no existe con ID: " + id);
        }
        promotionRepository.deleteById(id);
    }
}
