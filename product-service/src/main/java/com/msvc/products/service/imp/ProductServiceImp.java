package com.msvc.products.service.imp;

import com.msvc.products.dto.mapper.ProductMapper;
import com.msvc.products.dto.request.ProductRequestDTO;
import com.msvc.products.dto.response.ProductResponseDTO;
import com.msvc.products.entity.Product;
import com.msvc.products.repository.ProductRepository;
import com.msvc.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

}
