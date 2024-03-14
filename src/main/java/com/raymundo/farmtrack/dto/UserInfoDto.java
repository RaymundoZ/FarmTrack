package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.Constants;
import com.raymundo.farmtrack.validation.EnumValid;
import com.raymundo.farmtrack.validation.UserEmailUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInfoDto(

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        @Email(message = Constants.EMAIL_MESSAGE)
        @UserEmailUnique(message = Constants.USER_EMAIL_UNIQUE_MESSAGE)
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String password,

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String name,

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String surname,

        @NotBlank(message = Constants.NOT_BLANK_MESSAGE)
        String patronymic,

        @NotNull(message = Constants.NOT_NULL_MESSAGE)
        @EnumValid(message = Constants.ENUM_VALID_MESSAGE)
        String role,

        @JsonIgnore
        String accessToken,

        @JsonIgnore
        String refreshToken
) {
}
