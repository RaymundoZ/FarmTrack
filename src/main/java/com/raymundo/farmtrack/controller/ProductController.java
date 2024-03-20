package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.ProductDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles product-related operations.
 * <p>
 * This class provides REST endpoints for registering, deleting, and retrieving products.
 * It utilizes the {@link ProductService} to perform these operations.
 *
 * @author RaymundoZ
 */
@Tag(name = "ProductController", description = "Controller class that handles product-related operations")
@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Endpoint for registering a product.
     * <p>
     * This method handles the registration of a product by accepting a POST request with a JSON body
     * containing the product information in the form of {@link ProductDto}. The product information
     * is validated using the {@link Valid} annotation. If the product is successfully registered
     * by the {@link ProductService}, the method returns a {@link ResponseEntity} with a success message
     * and the registered product information in {@link ProductDto} format, along with an HTTP status code
     * 201 (CREATED).
     *
     * @param productDto A {@link ProductDto} object containing the product information for registration.
     * @return A {@link ResponseEntity} containing a success message and the registered product information upon successful registration.
     */
    @Operation(summary = "Endpoint for registering a product")
    @PostMapping
    public ResponseEntity<SuccessDto<ProductDto>> registerProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto<>(
                HttpStatus.CREATED.value(),
                "Product successfully created",
                productService.registerProduct(productDto)
        ));
    }

    /**
     * Endpoint for deleting a product.
     * <p>
     * This method handles the deletion of a product by accepting a DELETE request with a path variable
     * specifying the name of the product to be deleted. If the product is successfully deleted
     * by the {@link ProductService}, the method returns a {@link ResponseEntity} with a success message
     * and the deleted product information in {@link ProductDto} format, along with an HTTP status code
     * 200 (OK).
     *
     * @param productName The name of the product to be deleted.
     * @return A {@link ResponseEntity} containing a success message and the deleted product information upon successful deletion.
     */
    @Operation(summary = "Endpoint for deleting a product")
    @DeleteMapping(value = "/{productName}")
    public ResponseEntity<SuccessDto<ProductDto>> deleteProduct(@PathVariable
                                                                @Parameter(description = "The name of the product to be deleted")
                                                                String productName) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Product successfully deleted",
                productService.deleteProduct(productName)
        ));
    }

    /**
     * Endpoint for retrieving all products.
     * <p>
     * This method handles the retrieval of all products by accepting a GET request.
     * It retrieves all products using the {@link ProductService} and returns a {@link ResponseEntity}
     * with a success message and a list of all products in {@link ProductDto} format, along with
     * an HTTP status code 200 (OK).
     *
     * @return A {@link ResponseEntity} containing a success message and a list of all products upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving all products")
    @GetMapping
    public ResponseEntity<SuccessDto<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Product list received successfully",
                productService.getAllProducts()
        ));
    }
}
