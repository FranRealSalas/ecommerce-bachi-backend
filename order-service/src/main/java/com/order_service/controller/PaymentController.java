package com.order_service.controller;

import com.order_service.dto.request.CreatePaymentRequestDTO;
import com.order_service.dto.response.PaymentResponseDTO;
import com.order_service.service.imp.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody CreatePaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserPayments(@PathVariable String username) {
        return ResponseEntity.ok(paymentService.getPaymentsByUser(username));
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam String collection_id,
            @RequestParam String collection_status,
            @RequestParam String payment_id,
            @RequestParam String status,
            @RequestParam String external_reference,
            @RequestParam String payment_type,
            @RequestParam String merchant_order_id,
            @RequestParam String preference_id) {

        return ResponseEntity.ok("Pago exitoso - Preference ID: " + preference_id);
    }

    @GetMapping("/failure")
    public ResponseEntity<String> paymentFailure(
            @RequestParam String collection_id,
            @RequestParam String collection_status,
            @RequestParam String payment_id,
            @RequestParam String status,
            @RequestParam String external_reference,
            @RequestParam String payment_type,
            @RequestParam String merchant_order_id,
            @RequestParam String preference_id) {

        return ResponseEntity.ok("Pago fallido - Preference ID: " + preference_id);
    }

    @GetMapping("/pending")
    public ResponseEntity<String> paymentPending(
            @RequestParam String collection_id,
            @RequestParam String collection_status,
            @RequestParam String payment_id,
            @RequestParam String status,
            @RequestParam String external_reference,
            @RequestParam String payment_type,
            @RequestParam String merchant_order_id,
            @RequestParam String preference_id) {

        return ResponseEntity.ok("Pago pendiente - Preference ID: " + preference_id);
    }
}