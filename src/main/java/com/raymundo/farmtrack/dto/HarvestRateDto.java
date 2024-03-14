package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.raymundo.farmtrack.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record HarvestRateDto(

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @Positive(message = Constants.POSITIVE_MESSAGE)
        Integer rate,

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate date,

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String product
) {
}
