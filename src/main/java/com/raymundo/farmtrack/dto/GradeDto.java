package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GradeDto(

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String user,

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @Min(value = 1, message = Constants.MIN_GRADE_MESSAGE)
        @Max(value = 5, message = Constants.MAX_GRADE_MESSAGE)
        Integer grade,

        @JsonProperty(value = "date", access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate createdDate
) {
}
