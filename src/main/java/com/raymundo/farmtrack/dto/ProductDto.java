package com.raymundo.farmtrack.dto;

import com.raymundo.farmtrack.validation.EnumValid;
import com.raymundo.farmtrack.validation.ProductNameUnique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductDto(

        @NotBlank(message = "Name should not be blank")
        @ProductNameUnique(message = "This name is already taken")
        String name,

        @NotNull(message = "Integer should not be null")
        @Positive(message = "Amount should be positive number")
        Integer amount,

        @NotBlank(message = "Measure should not be null")
        @EnumValid(message = "Measure should be valid")
        String measure
) {
}
