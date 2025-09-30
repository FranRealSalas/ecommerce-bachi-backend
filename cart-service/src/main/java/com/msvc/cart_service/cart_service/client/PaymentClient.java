// CartClient completo usando RestTemplate
package com.msvc.cart_service.cart_service.client;

import com.msvc.cart_service.cart_service.dto.request.CreatePaymentRequestDTO;
import com.msvc.cart_service.cart_service.dto.response.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentClient {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Crear un nuevo pago
     * Conecta con: POST /api/payments/create
     */
    public PaymentResponseDTO createPayment(CreatePaymentRequestDTO request) {
        return restTemplate.postForObject(
                "http://order-service/api/payments/create",
                request,
                PaymentResponseDTO.class
        );
    }

    /**
     * Obtener pagos de un usuario
     * Conecta con: GET /api/payments/user/{username}
     */
    public Object getUserPayments(String username) {
        return restTemplate.getForObject(
                "http://order-service/api/payments/user/" + username,
                Object.class
        );
    }
}