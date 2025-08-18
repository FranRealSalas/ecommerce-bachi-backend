package com.msvc.cart_service.cart_service.service.imp;

import com.msvc.cart_service.cart_service.client.ProductClient;
import com.msvc.cart_service.cart_service.dto.request.CartRequestDTO;
import com.msvc.cart_service.cart_service.dto.response.CartItemResponseDTO;
import com.msvc.cart_service.cart_service.dto.response.CartResponseDTO;
import com.msvc.cart_service.cart_service.dto.response.ProductResponseDTO;
import com.msvc.cart_service.cart_service.entity.Cart;
import com.msvc.cart_service.cart_service.entity.CartItem;
import com.msvc.cart_service.cart_service.repository.CartRepository;
import com.msvc.cart_service.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public CartResponseDTO getCart(String username) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsername(username);

                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        return mapToResponseDTO(cart);
    }

    @Override
    public CartResponseDTO addItemToCart(String username, CartRequestDTO cartRequestDTO) {
        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUsername(username);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(cartRequestDTO.getProductCartId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartRequestDTO.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(cartRequestDTO.getProductCartId());
            newItem.setQuantity(cartRequestDTO.getQuantity());

            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return mapToResponseDTO(cart);
    }

    @Override
    public CartResponseDTO removeItemFromCart(String username, Long productId) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        cartRepository.save(cart);
        return mapToResponseDTO(cart);
    }

    @Override
    public CartResponseDTO removeOneFromCart(String username, Long productId) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                cart.getItems().remove(item);
            }
            cartRepository.save(cart);
        }

        return mapToResponseDTO(cart);
    }

    @Override
    public void clearCart(String username) {
        cartRepository.deleteByUsername(username);
    }

    // Método para mapear Cart -> CartResponseDTO
    private CartResponseDTO mapToResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = CartResponseDTO.builder()
                .username(cart.getUsername())
                .items(cart.getItems().stream()
                        .map(item -> {
                                    ProductResponseDTO productResponseDTO = productClient.getProductById(item.getProductId());
                                    return CartItemResponseDTO.builder()
                                            .productId(item.getProductId())
                                            .productName(productResponseDTO.getName())
                                            .productPrice(productResponseDTO.getPrice())
                                            .totalItemPrice(productResponseDTO.getPrice().multiply(new BigDecimal(item.getQuantity())))
                                            .quantity(item.getQuantity())
                                            .build();
                                }
                        ).collect(Collectors.toList()))
                .build();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BigDecimal num : cartResponseDTO.getItems().stream().map(CartItemResponseDTO::getTotalItemPrice).toList()) {
            totalPrice = totalPrice.add(num);
        }
        cartResponseDTO.setTotalPrice(totalPrice);

        return cartResponseDTO;
    }
}
