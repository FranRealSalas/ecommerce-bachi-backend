package com.order_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {

    private UserResponseDTO buyerId;

    private List<ProductResponseDTO> products;

    private BigDecimal totalPrice;

}
