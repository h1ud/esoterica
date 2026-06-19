package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.clientDTO;
import com.webproject.esoteria.domain.entity.Client;
import com.webproject.esoteria.repository.clientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class clientService {
    private final clientRepository clientRepository;

    public clientService(clientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public clientDTO create(clientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setPassword_hash(dto.getPassword_hash());
        client.setDni(dto.getDni());
        client.setBirthday_date(dto.getBirthday_date());
        client.setCreate_date(LocalDateTime.now());

        Client saved = clientRepository.save(client);
        return mapToDTO(saved);
    }

    public clientDTO getById(Long id) {
        return clientRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    public clientDTO update(Long id, clientDTO dto) {
        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setPassword_hash(dto.getPassword_hash());
                    existing.setDni(dto.getDni());
                    existing.setBirthday_date(dto.getBirthday_date());
                    return mapToDTO(clientRepository.save(existing));
                })
                .orElse(null);
    }

    public clientDTO delete(Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    clientDTO deleted = mapToDTO(client);
                    clientRepository.delete(client);
                    return deleted;
                })
                .orElse(null);
    }

    public List<clientDTO> listAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private clientDTO mapToDTO(Client client) {
        return new clientDTO(
                client.getId(),
                client.getName(),
                client.getPassword_hash(),
                client.getDni(),
                client.getBirthday_date(),
                client.getCreate_date()
        );
    }
}

