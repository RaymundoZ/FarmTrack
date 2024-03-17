package com.raymundo.farmtrack.util.validation;

import com.raymundo.farmtrack.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator implements ConstraintValidator<ProductNameUnique, String> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return productRepository.findByName(value).isEmpty();
    }
}
