package com.raymundo.farmtrack.dto;

import com.raymundo.farmtrack.util.validation.EnumValid;
import com.raymundo.farmtrack.util.validation.ProductNameUnique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.raymundo.farmtrack.util.Constants.*;

public record ProductDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @ProductNameUnique(message = PRODUCT_NAME_UNIQUE_MESSAGE)
        String name,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Positive(message = POSITIVE_MESSAGE)
        Integer amount,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(message = ENUM_VALID_MESSAGE)
        String measure
) {
}
