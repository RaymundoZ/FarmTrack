package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

    @PostMapping(value = "/register")
    public ResponseEntity<SuccessDto<UserInfoDto>> registerUser(@Valid @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto<>(
                HttpStatus.CREATED.value(),
                "Registration succeed",
                authService.registerUser(userInfoDto)
        ));
    }

    @PostMapping(value = "/block/{userEmail}")
    public ResponseEntity<SuccessDto<UserInfoDto>> blockUser(@PathVariable String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "User has been successfully blocked",
                authService.blockUser(userEmail)
        ));
    }

    @PostMapping(value = "/unblock/{userEmail}")
    public ResponseEntity<SuccessDto<UserInfoDto>> unblockUser(@PathVariable String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "User has been successfully unblocked",
                authService.unblockUser(userEmail)
        ));
    }

    private Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }
}
