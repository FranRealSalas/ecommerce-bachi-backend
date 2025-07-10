package com.order_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {

    private Long buyerId;

    private List<Long> productsIds;

    private String shippingAddress;

    private String paymentMethod;

    private String notes;

    private BigDecimal totalPrice;
}
