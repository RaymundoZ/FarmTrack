package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.raymundo.farmtrack.util.Measure;

public record StatisticsItemDto(

        String product,

        Integer amount,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Measure measure
) {
}
