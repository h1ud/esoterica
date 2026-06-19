package com.webproject.esoteria.domain.mapper;

import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.domain.entity.Product;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface productMapper {
   @Mapping(source = "product_name", target = "productName")
   @Mapping(target = "id", ignore = true)
   Product toEntity(productDTO dto);

   @Mapping(source = "productName", target = "product_name")
   productDTO toDto(Product entity);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   @Mapping(source = "product_name", target = "productName")
   @Mapping(target = "id", ignore = true)
   void updateProductFromDto(productDTO dto, @MappingTarget Product entity);

   default LocalDateTime map(String value) {
      if (value == null || value.isBlank()) {
         return null;
      }

      try {
         return LocalDateTime.parse(value);
      } catch (Exception ignored) {
         return LocalDate.parse(value).atStartOfDay();
      }
   }

   default String map(LocalDateTime value) {
      return value != null ? value.toString() : null;
   }
}
