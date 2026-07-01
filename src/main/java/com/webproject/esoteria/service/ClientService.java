package com.webproject.esoteria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.webproject.esoteria.domain.dto.ClientDTO;
import com.webproject.esoteria.domain.dto.ClientSaveDTO;
import com.webproject.esoteria.domain.entity.Client;
import com.webproject.esoteria.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public ClientDTO getClientByDni(String dni) {
        Client c = clientRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("cliente no encontrado con DNI: " + dni));
        return new ClientDTO(
                c.getId(),
                c.getName(),
                c.getDni(),
                c.getBirthdayDate(),
                c.getCreateDate()
        );
    }

    @Transactional
    public void saveClient(ClientSaveDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setDni(dto.dni());
        client.setBirthdayDate(dto.birthdayDate());

        client.setPasswordHash(passwordEncoder.encode(dto.password()));

        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(Long id, ClientSaveDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("cliente no encontrado con ID: " + id));

        client.setName(dto.name());
        client.setDni(dto.dni());
        if (dto.birthdayDate() != null) {
            client.setBirthdayDate(dto.birthdayDate());
        }

        if (dto.password() != null && !dto.password().isEmpty()) {
            client.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("cliente no encontrado con ID: " + id);
        }
        clientRepository.deleteById(id);
    }
}