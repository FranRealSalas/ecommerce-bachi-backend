package com.msvc.cart_service.cart_service.dto.response;

import com.msvc.cart_service.cart_service.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private String username;

    private BigDecimal totalPrice;

    private List<CartItemResponseDTO> items;
}
