package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.raymundo.farmtrack.util.Constants.*;

public record GradeDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String user,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Min(value = 1, message = MIN_GRADE_MESSAGE)
        @Max(value = 5, message = MAX_GRADE_MESSAGE)
        Integer grade,

        @JsonProperty(value = "date", access = READ_ONLY)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate createdDate
) {
}
