package com.msvc.products.dto.mapper;

import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductResponseDTO toDto(Product product);
}
