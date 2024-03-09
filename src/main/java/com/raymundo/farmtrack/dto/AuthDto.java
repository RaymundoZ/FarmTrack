package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthDto(

        @NotBlank(message = "Email should not be blank")
        @Email(message = "Email should be valid")
        @Size(max = 50, message = "Email should not exceed 50 characters")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password should not be blank")
        @Size(max = 50, message = "Password should not exceed 50 characters")
        String password
) {
}
