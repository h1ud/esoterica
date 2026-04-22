package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.entity.Username;
import com.webproject.esoteria.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/username")
public class usuarioController {
    @Autowired
    private usuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Username> guardar(@RequestBody Username user){
        return ResponseEntity.ok(usuarioService.crearUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Username> buscar(@PathVariable Long id) {
       return ResponseEntity.ok(usuarioService.obtenerUser(id));
    }

}
