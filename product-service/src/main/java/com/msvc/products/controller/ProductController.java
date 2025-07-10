package com.msvc.products.controller;

import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        System.out.println("DTO recibido: " + productRequestDTO);
        return productService.createProduct(productRequestDTO);
    }

    @GetMapping
    public List<ProductResponseDTO> listAllProducts(){
        return productService.listAllProducts();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name){
        Optional<ProductResponseDTO> productOptional = productService.findProductByName(name);
        if (productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(productOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Product not found"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category){
        List<ProductResponseDTO> products = productService.listProductsByCategory(category);
        if (!products.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Products not found in category" + category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<ProductResponseDTO> productOptional = productService.findProductById(id);
        if (productOptional.isPresent()) {
                productService.deleteProductById(id);
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
