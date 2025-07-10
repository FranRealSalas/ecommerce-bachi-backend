package com.msvc.products.dto.mapper;

import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public abstract class ProductMapper {

    public abstract ProductResponseDTO toDto(Product product);

}
