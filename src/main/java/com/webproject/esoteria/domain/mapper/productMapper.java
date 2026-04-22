package com.webproject.esoteria.domain.mapper;

import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface productMapper {
   Product toEntity(productDTO dto);
   productDTO toDto(Product entity);
    void updateProductFromDto(productDTO dto,@MappingTarget Product entity);
}
