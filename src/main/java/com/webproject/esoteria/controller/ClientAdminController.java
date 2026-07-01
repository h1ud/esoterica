package com.webproject.esoteria.controller;


import com.webproject.esoteria.domain.dto.ClientDTO;
import com.webproject.esoteria.domain.dto.ClientSaveDTO;
import com.webproject.esoteria.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/clients")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientAdminController {
    private final ClientService clientService;

    public ClientAdminController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ClientDTO> getClientByDni(@PathVariable String dni) {
        return ResponseEntity.ok(clientService.getClientByDni(dni));
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody ClientSaveDTO dto) {
        clientService.saveClient(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @RequestBody ClientSaveDTO dto) {
        clientService.updateClient(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
