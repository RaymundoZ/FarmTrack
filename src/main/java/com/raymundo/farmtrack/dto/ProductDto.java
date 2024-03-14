package com.raymundo.farmtrack.dto;

import com.raymundo.farmtrack.util.Constants;
import com.raymundo.farmtrack.validation.EnumValid;
import com.raymundo.farmtrack.validation.ProductNameUnique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductDto(

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        @ProductNameUnique(message = Constants.PRODUCT_NAME_UNIQUE_MESSAGE)
        String name,

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @Positive(message = Constants.POSITIVE_MESSAGE)
        Integer amount,

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        @EnumValid(message = Constants.ENUM_VALID_MESSAGE)
        String measure
) {
}
