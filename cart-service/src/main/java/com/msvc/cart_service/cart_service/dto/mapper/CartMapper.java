package com.msvc.cart_service.cart_service.dto.mapper;

import com.msvc.cart_service.cart_service.dto.response.CartResponseDTO;
import com.msvc.cart_service.cart_service.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CartMapper.class})
public abstract class CartMapper {

        public abstract CartResponseDTO toDto(Cart cart);

}
