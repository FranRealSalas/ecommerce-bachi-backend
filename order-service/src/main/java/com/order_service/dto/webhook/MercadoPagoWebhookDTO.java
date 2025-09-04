package com.order_service.dto.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MercadoPagoWebhookDTO {
    private String resource;
    private String topic;
    private Long id;
    private String type;
    private String action;
    private String api_version;
    private String date_created;
    private String user_id;
    private String live_mode;
}