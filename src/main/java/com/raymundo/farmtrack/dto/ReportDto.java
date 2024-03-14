package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.Constants;
import com.raymundo.farmtrack.util.Measure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReportDto(

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String product,

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @Positive(message = Constants.POSITIVE_MESSAGE)
        Integer amount,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Measure measure,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String user,

        @JsonProperty(value = "rate_left", access = JsonProperty.Access.READ_ONLY)
        Integer rateLeft
) {
}
