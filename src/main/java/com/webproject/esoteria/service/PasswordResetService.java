package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.PasswordResetDTO;
import com.webproject.esoteria.domain.dto.PasswordResetSaveDTO;
import com.webproject.esoteria.domain.entity.PasswordReset;
import com.webproject.esoteria.repository.PasswordResetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PasswordResetService {

    private final PasswordResetRepository repository;

    public PasswordResetService(PasswordResetRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveRequest(PasswordResetSaveDTO dto) {

        PasswordReset passwordReset = new PasswordReset(
                dto.name(),
                dto.lastName(),
                dto.username(),
                dto.email()
        );
        repository.save(passwordReset);
    }

    @Transactional(readOnly = true)
    public List<PasswordResetDTO> getAllRequests() {
        return repository.findAll().stream()
                .map(pr -> new PasswordResetDTO(
                        pr.getId(),
                        pr.getName(),
                        pr.getLastName(),
                        pr.getUsername(),
                        pr.getEmail(),
                        pr.getCreatedAt()
                ))
                .toList();
    }
}