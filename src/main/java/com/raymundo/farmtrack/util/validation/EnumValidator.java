package com.raymundo.farmtrack.util.validation;

import com.raymundo.farmtrack.util.enumeration.Measure;
import com.raymundo.farmtrack.util.enumeration.Role;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Custom validator for validating enums.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to validate enum values
 * annotated with the {@link EnumValid} annotation. It checks if the provided enum value exists
 * in one of the specified enum classes (Measure, Role, or TokenType).
 *
 * @author RaymundoZ
 * @see EnumValid
 * @see Measure
 * @see Role
 * @see TokenType
 */
@Component
public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    /**
     * Checks if the provided enum value is valid.
     * <p>
     * This method validates the provided enum value by checking if it exists in one of the
     * specified enum classes (Measure, Role, or TokenType). It returns true if the value is valid,
     * and false otherwise.
     *
     * @param value   The enum value to be validated.
     * @param context The validation context.
     * @return True if the enum value is valid, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        boolean measureExist = Arrays.stream(Measure.values())
                .anyMatch(measure -> measure.toString().equals(value.toUpperCase()));
        boolean roleExist = Arrays.stream(Role.values())
                .anyMatch(role -> role.toString().equals(value.toUpperCase()));
        boolean tokenTypeExist = Arrays.stream(TokenType.values())
                .anyMatch(tokenType -> tokenType.toString().equals(value.toUpperCase()));
        return measureExist || roleExist || tokenTypeExist;
    }
}
