package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record StatisticsDto(

        @JsonProperty(value = "start_date", access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Start date should not be null")
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate startDate,

        @JsonProperty(value = "end_date", access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "End date should not be null")
        @JsonFormat(pattern = "dd.MM.yyyy")
        LocalDate endDate,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String user,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<StatisticsItemDto> statistics
) {
}
