package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.UserDTO;
import com.webproject.esoteria.domain.dto.UserSaveDTO;
import com.webproject.esoteria.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserAdminController {

    private final UserService userService;

    // Inyección limpia por Constructor (Igual que ClientAdminController)
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserSaveDTO dto) {
        userService.saveUser(dto); // Llama al método void del servicio
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserSaveDTO dto) {
        userService.updateUser(id, dto); // Llama al método void del servicio
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}