package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record HarvestRateDto(

        @NotNull(message = "Rate should not be null")
        @Positive(message = "Rate should be a positive number")
        Integer rate,

        @NotNull(message = "Date should not be null")
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate date,

        @NotBlank(message = "Product should not be blank")
        String product
) {
}
