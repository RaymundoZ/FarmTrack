package com.raymundo.farmtrack.util.validation;

import com.raymundo.farmtrack.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Custom validator for checking the uniqueness of product names.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to validate the uniqueness of product names
 * annotated with the {@link ProductNameUnique} annotation. It checks if the provided product name does not
 * already exist in the database.
 *
 * @author RaymundoZ
 * @see ProductNameUnique
 * @see ProductRepository
 */
@Component
@RequiredArgsConstructor
public class ProductValidator implements ConstraintValidator<ProductNameUnique, String> {

    private final ProductRepository productRepository;

    /**
     * Checks if the provided product name is unique.
     * <p>
     * This method validates the provided product name by checking if it exists in the database.
     * It returns true if the product name is unique (not found in the database), and false otherwise.
     *
     * @param value   The product name to be validated.
     * @param context The validation context.
     * @return True if the product name is unique, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return productRepository.findByName(value).isEmpty();
    }
}
