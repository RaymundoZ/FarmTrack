package com.raymundo.farmtrack.validation;

import com.raymundo.farmtrack.util.Measure;
import com.raymundo.farmtrack.util.Role;
import com.raymundo.farmtrack.util.TokenType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean measureExist = Arrays.stream(Measure.values())
                .anyMatch(measure -> measure.toString().equals(value.toUpperCase()));
        boolean roleExist = Arrays.stream(Role.values())
                .anyMatch(role -> role.toString().equals(value.toUpperCase()));
        boolean tokenTypeExist = Arrays.stream(TokenType.values())
                .anyMatch(tokenType -> tokenType.toString().equals(value.toUpperCase()));
        return measureExist || roleExist || tokenTypeExist;
    }
}
