package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.raymundo.farmtrack.util.enumeration.Measure;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public record StatisticsItemDto(

        String product,

        Integer amount,

        @JsonFormat(shape = STRING)
        Measure measure
) {
}
