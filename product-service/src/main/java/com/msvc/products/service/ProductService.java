package com.msvc.products.service;

import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO productDTO);

    ResponseEntity<Map<String, Object>> uploadProductImage(Long productId, MultipartFile file);

    Optional<ProductResponseDTO> findProductByName(String productName);

    List<ProductResponseDTO> listProductsByCategory(String productCategory);

    Optional<ProductResponseDTO> findProductById(Long id);

    List<ProductResponseDTO> listAllProducts();

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productDTO);

    void deleteProductById(Long id);

}
