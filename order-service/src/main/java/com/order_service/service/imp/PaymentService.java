package com.order_service.service.imp;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.order_service.dto.request.CreatePaymentRequestDTO;
import com.order_service.dto.response.CartItemResponseDTO;
import com.order_service.dto.response.PaymentResponseDTO;
import com.order_service.entity.Payment;
import com.order_service.entity.PaymentItem;
import com.order_service.entity.PaymentStatus;
import com.order_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${mercadopago.success-url}")
    private String successUrl;

    @Value("${mercadopago.failure-url}")
    private String failureUrl;

    @Value("${mercadopago.pending-url}")
    private String pendingUrl;

    @Transactional
    public PaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {
        try {
            log.info("Creating payment for user: {}", request.getUsername());

            // Generar referencia externa única
            String externalReference = generateExternalReference();

            // Crear items para MercadoPago
            List<PreferenceItemRequest> items = createPreferenceItems(request.getItems());

            // Crear URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .pending(pendingUrl)
                    .build();

            // Crear preferencia de pago
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(externalReference)
                    .autoReturn("approved")
                    .build();

            // Llamar a MercadoPago API
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Guardar pago en base de datos
            Payment payment = createPaymentEntity(
                    request, preference.getId(), externalReference);

            Payment savedPayment = paymentRepository.save(payment);

            log.info("Payment created successfully with ID: {}", savedPayment.getId());

            return PaymentResponseDTO.builder()
                    .paymentId(savedPayment.getId())
                    .preferenceId(preference.getId())
                    .initPoint(preference.getInitPoint())
                    .sandboxInitPoint(preference.getSandboxInitPoint())
                    .status(savedPayment.getStatus().name())
                    .externalReference(externalReference)
                    .build();

        } catch (MPException | MPApiException e) {
            log.error("Error creating MercadoPago preference: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating payment preference", e);
        } catch (Exception e) {
            log.error("Unexpected error creating payment: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error creating payment", e);
        }
    }

    @Transactional
    public void updatePaymentStatus(Long mercadoPagoPaymentId, String status, String statusDetail) {
        try {
            log.info("Updating payment status for MP Payment ID: {}", mercadoPagoPaymentId);

            // Buscar pago en base de datos
            Payment payment = paymentRepository
                    .findByMercadoPagoPaymentId(mercadoPagoPaymentId)
                    .orElseThrow(() -> new RuntimeException("Payment not found with MP ID: " + mercadoPagoPaymentId));

            // Actualizar estado
            payment.setMercadoPagoStatus(status);
            payment.setMercadoPagoStatusDetail(statusDetail);
            payment.setStatus(mapMercadoPagoStatus(status));

            paymentRepository.save(payment);

            log.info("Payment status updated successfully for payment ID: {}", payment.getId());

        } catch (Exception e) {
            log.error("Error updating payment status: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating payment status", e);
        }
    }

    Payment getPaymentByPreferenceId(String preferenceId) {
        return paymentRepository.findByPreferenceId(preferenceId)
                .orElseThrow(() -> new RuntimeException("Payment not found with preference ID: " + preferenceId));
    }

    public List<Payment> getPaymentsByUser(String username) {
        return paymentRepository.findByUsername(username);
    }

    private List<PreferenceItemRequest> createPreferenceItems(List<CartItemResponseDTO> cartItems) {
        List<PreferenceItemRequest> items = new ArrayList<>();

        for (CartItemResponseDTO cartItem : cartItems) {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(cartItem.getProductName())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getProductPrice())
                    .currencyId("ARS")
                    .build();
            items.add(item);
        }

        return items;
    }

    private Payment createPaymentEntity(
            CreatePaymentRequestDTO request, String preferenceId, String externalReference) {

        Payment payment = Payment.builder()
                .username(request.getUsername())
                .totalAmount(request.getTotalPrice())
                .currency("ARS")
                .status(PaymentStatus.PENDING)
                .preferenceId(preferenceId)
                .externalReference(externalReference)
                .build();

        // Crear items del pago
        List<PaymentItem> paymentItems = new ArrayList<>();
        for (CartItemResponseDTO cartItem : request.getItems()) {
            PaymentItem paymentItem = PaymentItem.builder()
                    .payment(payment)
                    .productId(cartItem.getProductId())
                    .productName(cartItem.getProductName())
                    .unitPrice(cartItem.getProductPrice())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalItemPrice())
                    .build();
            paymentItems.add(paymentItem);
        }

        payment.setItems(paymentItems);
        return payment;
    }

    private PaymentStatus mapMercadoPagoStatus(String mpStatus) {
        return switch (mpStatus.toLowerCase()) {
            case "approved" -> PaymentStatus.APPROVED;
            case "authorized" -> PaymentStatus.AUTHORIZED;
            case "in_process" -> PaymentStatus.IN_PROCESS;
            case "in_mediation" -> PaymentStatus.IN_MEDIATION;
            case "rejected" -> PaymentStatus.REJECTED;
            case "cancelled" -> PaymentStatus.CANCELLED;
            case "refunded" -> PaymentStatus.REFUNDED;
            case "charged_back" -> PaymentStatus.CHARGED_BACK;
            default -> PaymentStatus.PENDING;
        };
    }

    private String generateExternalReference() {
        return "ORDER-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
