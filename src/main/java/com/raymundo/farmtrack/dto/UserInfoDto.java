package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.validation.EnumValid;
import com.raymundo.farmtrack.util.validation.UserEmailUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.raymundo.farmtrack.util.Constants.*;

public record UserInfoDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Email(message = EMAIL_MESSAGE)
        @UserEmailUnique(message = USER_EMAIL_UNIQUE_MESSAGE)
        String email,

        @JsonProperty(access = WRITE_ONLY)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String password,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String name,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String surname,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        String patronymic,

        @NotNull(message = NOT_NULL_MESSAGE)
        @EnumValid(message = ENUM_VALID_MESSAGE)
        String role,

        @JsonIgnore
        String accessToken,

        @JsonIgnore
        String refreshToken
) {
}
