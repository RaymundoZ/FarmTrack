package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.ProductDto;
import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.mapper.ProductMapper;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.service.ProductService;
import com.raymundo.farmtrack.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto registerProduct(ProductDto product) {
        ProductEntity productEntity = productMapper.toEntity(product);
        return productMapper.toDto(productRepository.save(productEntity));
    }

    @Override
    public ProductDto deleteProduct(String productName) {
        ProductEntity product = productRepository.findByName(productName).orElseThrow(() ->
                NotFoundException.Code.PRODUCT_NOT_FOUND.get(productName));
        productRepository.delete(product);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }
}
