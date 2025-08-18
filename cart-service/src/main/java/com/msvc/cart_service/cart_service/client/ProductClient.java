package com.msvc.cart_service.cart_service.client;

import com.msvc.cart_service.cart_service.dto.response.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public ProductResponseDTO getProductById(Long productId) {
        return restTemplate.getForObject(
                "http://product-service/api/products/id/" + productId,
                ProductResponseDTO.class
        );
    }
}