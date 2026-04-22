package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Username;
import com.webproject.esoteria.domain.mapper.userMapper;
import com.webproject.esoteria.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {

    @Autowired
    private usuarioRepository usuarioRepository;
    @Autowired
    private userMapper userMapper;

    public userDTO create(userDTO dto) {
        Username entity= userMapper.toEntity(dto);
        Username saved = usuarioRepository.save(entity);
        return userMapper.toDto(saved);
    }

    public userDTO getById(Long id) {
        return usuarioRepository
                .findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public userDTO delete(Long id) {
        return usuarioRepository
                .findById(id)
                .map(entity -> {
                    usuarioRepository.delete(entity);
                    return userMapper.toDto(entity);
                }).orElse(null);
    }

     public userDTO update(Long id, userDTO dto) {
        return usuarioRepository
                .findById(id)
                .map(entity -> {
                    userMapper.updateEntityFromDTO(dto, entity);
                    Username updated = usuarioRepository.save(entity);
                    return userMapper.toDto(updated);
                }).orElse(null);
    }
    public List<userDTO> listAll(){
        return usuarioRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

}
