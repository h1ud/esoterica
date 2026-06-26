package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.UserSaveDTO;
import com.webproject.esoteria.domain.entity.User;
import com.webproject.esoteria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserAdminController {
    @Autowired
    private UserService userService;

    // Listar todos los usuarios en el panel
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Registrar un nuevo administrador o vendedor
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserSaveDTO dto) {
        try {
            User newUser = userService.saveUser(dto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserSaveDTO dto) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
