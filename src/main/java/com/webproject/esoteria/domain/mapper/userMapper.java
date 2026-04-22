package com.webproject.esoteria.domain.mapper;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Username;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface userMapper {
    userDTO toDto(Username entity);
    Username toEntity(userDTO dto);
    void updateEntityFromDTO(userDTO dto, @MappingTarget Username entity);
}
