package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.PasswordResetDTO;
import com.webproject.esoteria.domain.dto.PasswordResetSaveDTO;
import com.webproject.esoteria.service.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password-resets")
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PasswordResetSaveDTO dto) {
        service.saveRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PasswordResetDTO>> getAll() {
        List<PasswordResetDTO> requests = service.getAllRequests();
        return ResponseEntity.ok(requests);
    }
}