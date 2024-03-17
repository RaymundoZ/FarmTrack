package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.raymundo.farmtrack.util.Constants.NOT_NULL_MESSAGE;

public record StatisticsDto(

        @JsonProperty(value = "start_date", access = WRITE_ONLY)
        @NotNull(message = NOT_NULL_MESSAGE)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate startDate,

        @JsonProperty(value = "end_date", access = WRITE_ONLY)
        @NotNull(message = NOT_NULL_MESSAGE)
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate endDate,

        @JsonProperty(access = READ_ONLY)
        String user,

        @JsonProperty(access = READ_ONLY)
        List<StatisticsItemDto> statistics
) {
}
