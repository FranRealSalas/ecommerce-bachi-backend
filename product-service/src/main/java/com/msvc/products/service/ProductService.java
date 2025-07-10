package com.msvc.products.service;

import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    Optional<ProductResponseDTO> findProductByName(String productName);

    List<ProductResponseDTO> listProductsByCategory(String productCategory);

    Optional<ProductResponseDTO> findProductById(Long id);

    List<ProductResponseDTO> listAllProducts();

    void deleteProductById(Long id);

}
