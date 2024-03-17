package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.raymundo.farmtrack.util.Constants.EMAIL_MESSAGE;
import static com.raymundo.farmtrack.util.Constants.NOT_BLANK_MESSAGE;

public record AuthDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Email(message = EMAIL_MESSAGE)
        String email,

        @JsonProperty(access = WRITE_ONLY)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String password
) {
}
