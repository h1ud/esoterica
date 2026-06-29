package com.webproject.esoteria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.webproject.esoteria.domain.dto.UserDTO;
import com.webproject.esoteria.domain.dto.UserSaveDTO;
import com.webproject.esoteria.domain.entity.User;
import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.repository.UserRepository;
import com.webproject.esoteria.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Inyección limpia por Constructor (Como en ClientService)
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // 1. Lógica para Leer todos los usuarios (GET)
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getUsername(),
                        u.getName(),
                        u.getLastName(),
                        u.getRole().getId(),
                        u.getRole().getRoleName(),
                        u.getCreateDate()
                ))
                .toList();
    }

    // 2. Lógica para Buscar usuario por ID (GET)
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return new UserDTO(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getLastName(),
                u.getRole().getId(),
                u.getRole().getRoleName(),
                u.getCreateDate()
        );
    }

    // 3. Lógica para Registrar Usuario (POST)
    public void saveUser(UserSaveDTO dto) {
        Role role = roleRepository.findById(dto.idRole())
                .orElseThrow(() -> new RuntimeException("El Rol especificado no existe con ID: " + dto.idRole()));
        User user = new User();
        user.setUsername(dto.username());
        user.setName(dto.name());
        user.setLastName(dto.lastName());
        user.setRole(role);

        // Encriptar la contraseña del formulario antes de ir a la BD
        user.setPasswordHash(passwordEncoder.encode(dto.password()));

        userRepository.save(user);
    }

    // 4. Lógica para Actualizar Usuario (PUT)
    public void updateUser(Long id, UserSaveDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        user.setUsername(dto.username());
        user.setName(dto.name());
        user.setLastName(dto.lastName());

        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        Role role = roleRepository.findById(dto.idRole())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.idRole()));
        user.setRole(role);

        userRepository.save(user); // JpaRepository detecta el ID y hace un UPDATE en vez de INSERT
    }

    // 5. Lógica para Eliminar Usuario (DELETE)
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}