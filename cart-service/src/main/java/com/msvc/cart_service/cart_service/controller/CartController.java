package com.msvc.cart_service.cart_service.controller;

import com.msvc.cart_service.cart_service.client.PaymentClient;
import com.msvc.cart_service.cart_service.dto.request.CartRequestDTO;
import com.msvc.cart_service.cart_service.dto.request.CreatePaymentRequestDTO;
import com.msvc.cart_service.cart_service.dto.response.CartResponseDTO;
import com.msvc.cart_service.cart_service.dto.response.PaymentResponseDTO;
import com.msvc.cart_service.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentClient paymentClient;

    @GetMapping("/{username}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable String username) {
        return ResponseEntity.ok(cartService.getCart(username));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @PostMapping("/{username}/checkout")
    public ResponseEntity<PaymentResponseDTO> checkout(@PathVariable String username) {
        // Obtener carrito desde el servicio
        CartResponseDTO cart = cartService.getCart(username);

        // Convertir carrito a request de pago
        CreatePaymentRequestDTO paymentRequest = CreatePaymentRequestDTO.builder()
                .username(username)
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .items(cart.getItems())
                .build();

        // Llamar al payment-service vía Feign
        PaymentResponseDTO payment = paymentClient.createPayment(paymentRequest);

        return ResponseEntity.ok(payment);
    }

    @PostMapping("/add/{username}")
    public ResponseEntity<CartResponseDTO> addItem(@PathVariable String username, @RequestBody CartRequestDTO cartRequestDTO) {
        return ResponseEntity.ok(cartService.addItemToCart(username, cartRequestDTO));
    }

    @DeleteMapping("/{username}/item/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable String username, @PathVariable Long productId) {
        cartService.removeItemFromCart(username, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}/item/{productId}/one")
    public ResponseEntity<Void> removeOneFromCart( @PathVariable String username, @PathVariable Long productId) {
        cartService.removeOneFromCart(username, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear/{username}")
    public ResponseEntity<Void> clearCart(@PathVariable String username) {
        cartService.clearCart(username);
        return ResponseEntity.noContent().build();
    }
}
