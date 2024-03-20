package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class that handles authentication and user-related operations.
 * <p>
 * This class provides REST endpoints for user authentication, registration, blocking, and unblocking.
 * It utilizes the {@link AuthService} to perform these operations.
 *
 * @author RaymundoZ
 */
@Tag(name = "AuthController", description = "Controller class that handles authentication and user-related operations")
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for user authentication.
     * <p>
     * This method handles user authentication by accepting a POST request with a JSON body
     * containing authentication credentials in the form of {@link AuthDto}. The credentials
     * are validated using the {@link AuthService}, and if authentication succeeds, the method
     * returns a {@link ResponseEntity} with a success message and the user information in
     * {@link UserInfoDto} format. Additionally, it sets cookies for the access token and
     * refresh token in the HTTP response.
     *
     * @param authDto  A {@link AuthDto} object containing user authentication credentials.
     * @param response The {@link HttpServletResponse} to set cookies for tokens.
     * @return A {@link ResponseEntity} containing a success message and user information upon successful authentication.
     */
    @Operation(summary = "Endpoint for user authentication")
    @PostMapping(value = "/login")
    public ResponseEntity<SuccessDto<UserInfoDto>> authenticate(@Valid @RequestBody AuthDto authDto, HttpServletResponse response) {
        UserInfoDto userInfoDto = authService.authenticate(authDto);
        response.addCookie(getCookie("access_token", null));
        response.addCookie(getCookie("refresh_token", null));
        response.addCookie(getCookie("access_token", userInfoDto.accessToken()));
        response.addCookie(getCookie("refresh_token", userInfoDto.refreshToken()));
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "Authorization succeed",
                userInfoDto
        ));
    }

    /**
     * Endpoint for user registration.
     * <p>
     * This method handles user registration by accepting a POST request with a JSON body
     * containing user information in the form of {@link UserInfoDto}. The user information
     * is validated using the {@link Valid} annotation. If registration succeeds, the method
     * returns a {@link ResponseEntity} with a success message and the registered user information
     * in {@link UserInfoDto} format, along with an HTTP status code 201 (CREATED).
     *
     * @param userInfoDto A {@link UserInfoDto} object containing user information for registration.
     * @return A {@link ResponseEntity} containing a success message and the registered user information upon successful registration.
     */
    @Operation(summary = "Endpoint for user registration")
    @PostMapping(value = "/register")
    public ResponseEntity<SuccessDto<UserInfoDto>> registerUser(@Valid @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto<>(
                HttpStatus.CREATED.value(),
                "Registration succeed",
                authService.registerUser(userInfoDto)
        ));
    }

    /**
     * Endpoint for blocking a user.
     * <p>
     * This method handles the blocking of a user by accepting a POST request with a path variable
     * specifying the email address of the user to be blocked. If the user is successfully blocked
     * by the {@link AuthService}, the method returns a {@link ResponseEntity} with a success message
     * and the updated user information in {@link UserInfoDto} format, along with an HTTP status code
     * 200 (OK).
     *
     * @param userEmail The email address of the user to be blocked.
     * @return A {@link ResponseEntity} containing a success message and the updated user information upon successful blocking.
     */
    @Operation(summary = "Endpoint for blocking a user")
    @PostMapping(value = "/block/{userEmail}")
    public ResponseEntity<SuccessDto<UserInfoDto>> blockUser(@PathVariable
                                                             @Parameter(description = "The email address of the user to be blocked")
                                                             String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "User has been successfully blocked",
                authService.blockUser(userEmail)
        ));
    }

    /**
     * Endpoint for unblocking a user.
     * <p>
     * This method handles the unblocking of a user by accepting a POST request with a path variable
     * specifying the email address of the user to be unblocked. If the user is successfully unblocked
     * by the {@link AuthService}, the method returns a {@link ResponseEntity} with a success message
     * and the updated user information in {@link UserInfoDto} format, along with an HTTP status code
     * 200 (OK).
     *
     * @param userEmail The email address of the user to be unblocked.
     * @return A {@link ResponseEntity} containing a success message and the updated user information upon successful unblocking.
     */
    @Operation(summary = "Endpoint for unblocking a user")
    @PostMapping(value = "/unblock/{userEmail}")
    public ResponseEntity<SuccessDto<UserInfoDto>> unblockUser(@PathVariable
                                                               @Parameter(description = "The email address of the user to be unblocked")
                                                               String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "User has been successfully unblocked",
                authService.unblockUser(userEmail)
        ));
    }

    /**
     * Constructs a cookie with the specified name and value.
     * <p>
     * This method constructs a {@link Cookie} object with the provided name and value.
     * It sets the cookie's path to "/" to make it accessible across the entire application.
     * The constructed cookie is then returned.
     *
     * @param name  The name of the cookie.
     * @param value The value of the cookie.
     * @return A {@link Cookie} object with the specified name and value.
     */
    private Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }
}
