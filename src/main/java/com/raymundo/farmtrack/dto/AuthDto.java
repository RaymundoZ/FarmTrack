package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthDto(

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        @Email(message = Constants.EMAIL_MESSAGE)
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String password
) {
}
