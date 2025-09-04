package com.order_service.controller;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.order_service.dto.webhook.MercadoPagoWebhookDTO;
import com.order_service.service.imp.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentService paymentService;

    @PostMapping("/mercadopago")
    public ResponseEntity<String> handleMercadoPagoWebhook(
            @RequestBody MercadoPagoWebhookDTO webhook,
            @RequestHeader(value = "x-signature", required = false) String signature,
            @RequestHeader(value = "x-request-id", required = false) String requestId) {

        try {
            log.info("Received MercadoPago webhook: {}", webhook);

            // Verificar que es un webhook de payment
            if (!"payment".equals(webhook.getTopic())) {
                log.info("Ignoring non-payment webhook: {}", webhook.getTopic());
                return ResponseEntity.ok("OK");
            }

            // Obtener información del pago desde MercadoPago
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(webhook.getId());

            // Actualizar estado del pago en nuestra base de datos
            paymentService.updatePaymentStatus(
                    payment.getId(),
                    payment.getStatus(),
                    payment.getStatusDetail()
            );

            log.info("Webhook processed successfully for payment ID: {}", webhook.getId());
            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            log.error("Error processing MercadoPago webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}