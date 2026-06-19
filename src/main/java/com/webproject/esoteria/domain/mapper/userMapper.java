package com.webproject.esoteria.domain.mapper;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.domain.entity.Role;
import com.webproject.esoteria.domain.entity.Username;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface userMapper {
    userDTO toDto(Username entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "closeSessions", ignore = true)
    @Mapping(target = "saleOperations", ignore = true)
    Username toEntity(userDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "create_date", ignore = true)
    @Mapping(target = "closeSessions", ignore = true)
    @Mapping(target = "saleOperations", ignore = true)
    void updateEntityFromDTO(userDTO dto, @MappingTarget Username entity);

    default Role map(Role role) {
        if (role == null) {
            return null;
        }

        Role copy = new Role();
        copy.setId(role.getId());
        copy.setRole_name(role.getRole_name());
        return copy;
    }
}
