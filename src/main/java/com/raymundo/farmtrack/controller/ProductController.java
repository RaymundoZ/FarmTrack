package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.ProductDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<SuccessDto<ProductDto>> registerProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto<>(
                HttpStatus.CREATED.value(),
                "Product successfully created",
                productService.registerProduct(productDto)
        ));
    }

    @DeleteMapping(value = "{productName}")
    public ResponseEntity<SuccessDto<ProductDto>> deleteProduct(@PathVariable String productName) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Product successfully deleted",
                productService.deleteProduct(productName)
        ));
    }

    @GetMapping
    public ResponseEntity<SuccessDto<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Product list recieved successfully",
                productService.getAllProducts()
        ));
    }
}
