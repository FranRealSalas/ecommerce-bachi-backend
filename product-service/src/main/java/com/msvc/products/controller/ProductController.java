package com.msvc.products.controller;

import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        log.info("DTO recibido: " + productRequestDTO);
        return productService.createProduct(productRequestDTO);
    }

    @PostMapping("/upload-product-image/{productId}")
    public ResponseEntity<Map<String, Object>> uploadProductImage(@RequestParam("file") MultipartFile file, @PathVariable Long productId) {
        return productService.uploadProductImage(productId, file);
    }

    @GetMapping("/uploads/products/{imageName:.+}")
    public ResponseEntity<Resource> viewProductImage(@PathVariable String imageName) {
        Path fileRoute = Paths.get("uploads/products").resolve(imageName).toAbsolutePath();
        Resource resource = null;

        if(!Files.exists(fileRoute)){
            return ResponseEntity.internalServerError().build();
        }

        try {
            resource = new UrlResource(fileRoute.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        assert resource != null;

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
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

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<ProductResponseDTO> productOptional = productService.findProductById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Product not found"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category){
        List<ProductResponseDTO> products = productService.listProductsByCategory(category);
        if (!products.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Products not found in category" + category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO){
        log.info("Actualizando producto con id: " + id + " con datos: " + productRequestDTO);
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
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
