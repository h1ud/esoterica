package com.webproject.esoteria.controller.auth;

import com.webproject.esoteria.domain.dto.LoginRequestDTO;
import com.webproject.esoteria.domain.dto.auth.AuthRequest;
import com.webproject.esoteria.domain.dto.auth.AuthResponseDTO;
import com.webproject.esoteria.domain.entity.User;
import com.webproject.esoteria.repository.UserRepository;
import com.webproject.esoteria.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDto) {
        try {
            System.out.println("=== INTENTO DE LOGIN ===");
            System.out.println("Username recibido: " + loginDto.username());

            User user = userRepository.findByUsername(loginDto.username())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD"));

            System.out.println("Usuario encontrado en BD: " + user.getUsername());

            boolean matches = passwordEncoder.matches(loginDto.password(), user.getPasswordHash());
            System.out.println("¿Contraseña coincide?: " + matches);

            if (!matches) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }

            System.out.println("Intentando generar token con jwtUtil...");
            String token = jwtUtil.generateToken(user.getUsername());
            System.out.println("Token generado con éxito: " + token);

            String roleName = user.getRole().getRoleName();
            System.out.println("Rol del usuario: " + roleName);

            AuthResponseDTO response = new AuthResponseDTO(token, user.getUsername(), roleName);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("login crash");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error interno: " + e.getMessage());
        }
    }
}