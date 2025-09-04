package com.order_service.dto.request;

import com.order_service.dto.response.CartItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequestDTO {
    private String username;
    private BigDecimal totalPrice;
    private Long totalItems;
    private java.util.List<CartItemResponseDTO> items;
}
