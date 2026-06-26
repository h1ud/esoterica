package com.webproject.esoteria.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.webproject.esoteria.domain.dto.UserSaveDTO;
import com.webproject.esoteria.domain.entity.User;
import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.repository.UserRepository;
import com.webproject.esoteria.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Registrar un nuevo usuario de staff
    public User saveUser(UserSaveDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        Role role = roleRepository.findById(dto.idRole())
                .orElseThrow(() -> new IllegalArgumentException("El Rol especificado no existe."));

        User user = new User();
        user.setRole(role);
        user.setUsername(dto.username());

        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        // TODO: Aquí aplicarás tu BCryptPasswordEncoder cuando unas la seguridad completa
        user.setName(dto.name());
        user.setLastName(dto.lastName());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Actualizar usuario existente
    public User updateUser(Long id, UserSaveDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setUsername(dto.username());
        user.setName(dto.name());
        user.setLastName(dto.lastName());

        // Solo cambiar contraseña si viene una nueva
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        Role role = roleRepository.findById(dto.idRole())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        user.setRole(role);

        return userRepository.save(user);
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no existe");
        }
        userRepository.deleteById(id);
    }
}
