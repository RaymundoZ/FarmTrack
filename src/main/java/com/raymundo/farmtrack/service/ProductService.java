package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto registerProduct(ProductDto product);

    ProductDto deleteProduct(String productName);

    List<ProductDto> getAllProducts();
}
