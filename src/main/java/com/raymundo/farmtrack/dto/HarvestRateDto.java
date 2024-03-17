package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

import static com.raymundo.farmtrack.util.Constants.*;

public record HarvestRateDto(

        @NotNull(message = NOT_NULL_MESSAGE)
        @Positive(message = POSITIVE_MESSAGE)
        Integer rate,

        @NotNull(message = NOT_NULL_MESSAGE)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate date,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String product
) {
}
