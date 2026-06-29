package com.webproject.esoteria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.webproject.esoteria.domain.dto.ClientDTO;
import com.webproject.esoteria.domain.dto.ClientSaveDTO;
import com.webproject.esoteria.domain.entity.Client;
import com.webproject.esoteria.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // 1. Lógica para Leer todos los clientes (GET)
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(c -> new ClientDTO(
                        c.getId(),
                        c.getName(),
                        c.getDni(),
                        c.getBirthdayDate(),
                        c.getCreateDate()
                ))
                .toList();
    }

    // 2. Lógica para Buscar cliente por ID (GET)
    public ClientDTO getClientByDni(String dni) {
        Client c = clientRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
        return new ClientDTO(
                c.getId(),
                c.getName(),
                c.getDni(),
                c.getBirthdayDate(),
                c.getCreateDate()
        );
    }

    // 3. Lógica para Registrar Cliente (POST)
    public void saveClient(ClientSaveDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setDni(dto.dni());
        client.setBirthdayDate(dto.birthdayDate());

        // Simulación: Aquí encriptarías la contraseña del formulario antes de ir a la BD
        client.setPasswordHash(passwordEncoder.encode(dto.password()));

        clientRepository.save(client);
    }

    // 4. Lógica para Actualizar Cliente (PUT)
    public void updateClient(Long id, ClientSaveDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        client.setName(dto.name());
        client.setDni(dto.dni());
        if (dto.birthdayDate() != null) {
            client.setBirthdayDate(dto.birthdayDate());
        }

        if (dto.password() != null && !dto.password().isEmpty()) {
            client.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        clientRepository.save(client);// JpaRepository detecta el ID y hace un UPDATE en vez de INSERT
    }
    // 5. Lógica para Eliminar Cliente (DELETE)
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clientRepository.deleteById(id);
    }
}