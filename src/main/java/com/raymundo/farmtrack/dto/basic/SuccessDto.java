package com.raymundo.farmtrack.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SuccessDto<T>(

        @JsonProperty(value = "status_code")
        Integer statusCode,

        String subject,

        T data
) {
}
