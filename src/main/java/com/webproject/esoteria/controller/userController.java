package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/username")
public class userController {
    @Autowired
    private userService userService;

    @PostMapping
    public ResponseEntity<userDTO> saveUser(@RequestBody userDTO user){
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<userDTO> getUserById(@PathVariable Long id) {
       return ResponseEntity.ok(userService.getById(id));
    }
    @GetMapping
    public ResponseEntity<List<userDTO>> listUsers(){
        return ResponseEntity.ok(userService.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<userDTO> updateUser(@PathVariable Long id, @RequestBody userDTO userDTO) {
            userDTO updatedUser = userService.update(id, userDTO);
            return updatedUser != null
                    ? ResponseEntity.ok(updatedUser)
                    : ResponseEntity.notFound().build();
        }
    @DeleteMapping("/{id}")
    public ResponseEntity<userDTO> deleteUser(@PathVariable Long id) {
            userDTO deletedUser = userService.delete(id);
            return deletedUser != null
                    ? ResponseEntity.ok(deletedUser)
                    : ResponseEntity.notFound().build();
        }
}
