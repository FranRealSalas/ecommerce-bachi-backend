package com.msvc.products.service.imp;

import com.msvc.products.dto.mapper.ProductMapper;
import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.entity.Product;
import com.msvc.products.repository.ProductRepository;
import com.msvc.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());

        Product savedProduct = productRepository.save(product);
        ProductResponseDTO productResponseDTO = productMapper.toDto(savedProduct);

        return productResponseDTO;
    }

    @Override
    public ResponseEntity<Map<String, Object>> uploadProductImage(Long productId, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        Optional<Product> optionalProduct = productRepository.findProductById(productId);

        if (!optionalProduct.isPresent()) {
            response.put("message", "Producto no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Product product = optionalProduct.get();

        if (!file.isEmpty()) {
            String fileName = product.getId().toString();
            File f = new File("uploads/products").getAbsoluteFile();
            f.mkdirs();
            Path fileRoute = Paths.get("uploads/products").resolve(fileName).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), fileRoute, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("message", "Error al subir la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            productRepository.save(product);

            response.put("product", product);
            response.put("message", "imagen subida correctamente");
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Override
    public Optional<ProductResponseDTO> findProductByName(String productName) {
        return productRepository.findProductByName(productName).map(product -> productMapper.toDto(product));
    }

    @Override
    public Optional<ProductResponseDTO> findProductById(Long id){
        return productRepository.findById(id).map(product -> productMapper.toDto(product));
    }

    @Override
    public List<ProductResponseDTO> listProductsByCategory(String productCategory) {
        return productRepository.findProductsByCategory(productCategory).stream().map(product -> productMapper.toDto(product)).toList();
    }

    @Override
    public List<ProductResponseDTO> listAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategory(productDTO.getCategory());

        Product saved = productRepository.save(existingProduct);
        return productMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

}
