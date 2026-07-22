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

    @Transactional(readOnly = true)
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(p -> new PromotionDTO(
                        p.getId(),
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getCode(),
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

    //sin uso
    @Transactional(readOnly = true)
    public PromotionDTO getPromotionById(long id) {
        Promotion p = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        return new PromotionDTO(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getUsername(),
                p.getCode(),
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

    @Transactional
    public void savePromotion(PromotionSaveDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("usuario admin no encontrado " + currentUsername));

        // Validar que el código no exista
        if (promotionRepository.findByCode(dto.code()).isPresent()) {
            throw new RuntimeException("Ya existe una promoción con el código: " + dto.code());
        }

        Promotion p = new Promotion();
        p.setUser(user);
        p.setCode(dto.code().toUpperCase());
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

    @Transactional
    public void updatePromotion(long id, PromotionSaveDTO dto) {
        Promotion p = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con ID: " + id));

        // Validar que el código no exista ya en otra promoción
        String newCode = dto.code().toUpperCase();
        if (!p.getCode().equals(newCode)) {
            promotionRepository.findByCode(newCode).ifPresent(existing -> {
                throw new RuntimeException("Ya existe otra promoción con el código: " + newCode);
            });
        }

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado con username: " + currentUsername));

        p.setUser(user);
        p.setCode(newCode);
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

    @Transactional
    public void deletePromotion(long id) {
        if (!promotionRepository.existsById(id)) {
            throw new RuntimeException("La promoción no existe con ID: " + id);
        }
        promotionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PromotionDTO getPromotionByCode(String code) {
        Promotion p = promotionRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Código promocional no válido: " + code));

        if (!p.isActive()) {
            throw new RuntimeException("La promoción " + code + " no está activa");
        }

        if (p.getEndDate() != null && p.getEndDate().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("La promoción " + code + " ha expirado");
        }

        return new PromotionDTO(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getUsername(),
                p.getCode(),
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
}
