package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.ProductDto;
import com.raymundo.farmtrack.entity.ProductEntity;
import com.raymundo.farmtrack.entity.ReportEntity;
import com.raymundo.farmtrack.mapper.ProductMapper;
import com.raymundo.farmtrack.repository.ProductRepository;
import com.raymundo.farmtrack.repository.ReportRepository;
import com.raymundo.farmtrack.service.ProductService;
import com.raymundo.farmtrack.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ProductService} interface for managing products.
 * <p>
 * This service provides methods for registering, deleting, and retrieving products.
 * It interacts with the product repository to perform CRUD operations on product entities.
 *
 * @author RaymundoZ
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;
    private final ProductMapper productMapper;

    /**
     * Registers a new product.
     * <p>
     * This method converts the provided {@link ProductDto} object to a {@link ProductEntity},
     * saves it using the product repository, and then converts the saved entity back to a
     * {@link ProductDto} object. The resulting DTO represents the registered product information.
     *
     * @param product A {@link ProductDto} object containing the product information to register.
     * @return A {@link ProductDto} object representing the registered product information.
     */
    @Override
    public ProductDto registerProduct(ProductDto product) {
        ProductEntity productEntity = productMapper.toEntity(product);
        return productMapper.toDto(productRepository.save(productEntity));
    }

    /**
     * Deletes a product by its name.
     * <p>
     * This method retrieves the product entity associated with the provided product name
     * from the product repository. If the product is not found, a {@link NotFoundException}
     * is thrown indicating that the product was not found. Otherwise, the product entity
     * is deleted from the repository. The deleted product entity is then converted to
     * a {@link ProductDto} object and returned.
     *
     * @param productName The name of the product to be deleted.
     * @return A {@link ProductDto} object representing the deleted product information.
     * @throws NotFoundException Thrown when the product with the specified name is not found.
     */
    @Override
    public ProductDto deleteProduct(String productName) {
        ProductEntity product = productRepository.findByName(productName).orElseThrow(() ->
                NotFoundException.Code.PRODUCT_NOT_FOUND.get(productName));
        List<ReportEntity> reports = reportRepository.findAllByProduct(product);
        reportRepository.deleteAll(reports);
        productRepository.delete(product);
        return productMapper.toDto(product);
    }

    /**
     * Retrieves a list of all products.
     * <p>
     * This method retrieves all product entities from the product repository and maps them
     * to {@link ProductDto} objects using {@link ProductMapper}. The resulting list of
     * product DTOs is returned.
     *
     * @return A list of {@link ProductDto} objects representing all products.
     */
    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }
}
