package com.msvc.cart_service.cart_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private BigDecimal totalItemPrice;

    private int quantity;
}
