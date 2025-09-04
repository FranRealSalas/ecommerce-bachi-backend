package com.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long paymentId;
    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;
    private String status;
    private String externalReference;
}