package com.raymundo.farmtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raymundo.farmtrack.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserInfoDto(

        @NotBlank(message = "Email should not be blank")
        @Email(message = "Email should be valid")
        @Size(max = 50, message = "Email should not exceed 50 characters")
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password should not be blank")
        @Size(max = 50, message = "Password should not exceed 50 characters")
        String password,

        @NotBlank(message = "Name should not be blank")
        @Size(max = 50, message = "Name should not exceed 50 characters")
        String name,

        @NotBlank(message = "Surname should not be blank")
        @Size(max = 50, message = "Surname should not exceed 50 characters")
        String surname,

        @NotBlank(message = "Patronymic should not be blank")
        @Size(max = 50, message = "Patronymic should not exceed 50 characters")
        String patronymic,

        @NotNull(message = "Role should not be blank")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Role role,

        @JsonIgnore
        String accessToken,

        @JsonIgnore
        String refreshToken
) {
}
