package com.msvc.cart_service.cart_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartRequestDTO {

    private Long productCartId;

    private BigDecimal productPrice;

    private int quantity;
}
