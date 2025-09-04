package com.msvc.cart_service.cart_service.controller;

import com.msvc.cart_service.cart_service.dto.request.CartRequestDTO;
import com.msvc.cart_service.cart_service.dto.response.CartResponseDTO;
import com.msvc.cart_service.cart_service.entity.Cart;
import com.msvc.cart_service.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{username}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable String username) {
        return ResponseEntity.ok(cartService.getCart(username));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CartResponseDTO> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
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
