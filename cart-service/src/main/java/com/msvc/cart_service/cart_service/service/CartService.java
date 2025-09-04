package com.msvc.cart_service.cart_service.service;

import com.msvc.cart_service.cart_service.dto.request.CartRequestDTO;
import com.msvc.cart_service.cart_service.dto.response.CartResponseDTO;

public interface CartService {

    CartResponseDTO getCart(String username);

    CartResponseDTO getCart(Long id);

    CartResponseDTO addItemToCart(String username, CartRequestDTO cartRequestDTO);

    CartResponseDTO removeItemFromCart(String username, Long productId);

    CartResponseDTO removeOneFromCart(String username, Long productId);

    void clearCart(String username);
}
