package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.clientDTO;
import com.webproject.esoteria.service.JwtService;
import com.webproject.esoteria.service.clientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ClientController {
    private final JwtService jwtService;
    private final clientService clientService;

    public ClientController(JwtService jwtService, clientService clientService) {
        this.jwtService = jwtService;
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<clientDTO>> listClients() {
        return ResponseEntity.ok(clientService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<clientDTO> getClientById(@PathVariable Long id) {
        clientDTO client = clientService.getById(id);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<clientDTO> createClient(
            @Valid @RequestBody clientDTO request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<clientDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody clientDTO request,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        clientDTO updatedClient = clientService.update(id, request);
        return updatedClient != null ? ResponseEntity.ok(updatedClient) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        HttpStatus accessError = resolveAdminAccessError(authorization);
        if (accessError != null) {
            return ResponseEntity.status(accessError).build();
        }

        clientDTO deletedClient = clientService.delete(id);
        return deletedClient != null ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private HttpStatus resolveAdminAccessError(String authorization) {
        if (!jwtService.isValidAuthorizationHeader(authorization)) {
            return HttpStatus.UNAUTHORIZED;
        }

        return jwtService.isValidAdminAuthorizationHeader(authorization) ? null : HttpStatus.FORBIDDEN;
    }
}

