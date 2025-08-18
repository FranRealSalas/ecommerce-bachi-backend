package com.msvc.cart_service.cart_service.repository;

import com.msvc.cart_service.cart_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUsername(String username);

    void deleteByUsername(String username);

}
