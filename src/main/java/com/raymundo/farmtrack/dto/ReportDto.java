package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.enumeration.Measure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.raymundo.farmtrack.util.Constants.*;

public record ReportDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String product,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Positive(message = POSITIVE_MESSAGE)
        Integer amount,

        @JsonProperty(access = READ_ONLY)
        @JsonFormat(shape = STRING)
        Measure measure,

        @JsonProperty(access = READ_ONLY)
        String user,

        @JsonProperty(value = "rate_left", access = READ_ONLY)
        Integer rateLeft
) {
}
