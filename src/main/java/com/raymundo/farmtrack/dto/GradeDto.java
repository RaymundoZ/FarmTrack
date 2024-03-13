package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GradeDto(

        @NotBlank(message = "User should not be blank")
        String user,

        @NotNull(message = "Grade should not be null")
        @Min(value = 1, message = "Grade should not be less than 1")
        @Max(value = 5, message = "Grade should not be more than 5")
        Integer grade,

        @JsonProperty(value = "date", access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate createdDate
) {
}
