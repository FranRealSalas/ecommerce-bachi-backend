package com.msvc.cart_service.cart_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String username;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "cart_id") // columna FK en CartItem
        private List<CartItem> items = new ArrayList<>();

        public void addOrUpdateItem(Long productId, int quantity) {
                Optional<CartItem> existingItemOptional = items.stream()
                        .filter(item -> item.getProductId().equals(productId))
                        .findFirst();

                if (existingItemOptional.isPresent()) {
                        existingItemOptional.get().setQuantity(existingItemOptional.get().getQuantity() + quantity);
                } else {
                        CartItem newItem = new CartItem();
                        newItem.setProductId(productId);
                        newItem.setQuantity(quantity);
                        items.add(newItem);
                }
        }
}
