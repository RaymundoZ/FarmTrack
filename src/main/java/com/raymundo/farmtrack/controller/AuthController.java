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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SuccessDto<UserInfoDto>> register(@Valid @RequestBody UserInfoDto userInfoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessDto<>(
                HttpStatus.CREATED.value(),
                "Registration succeed",
                authService.register(userInfoDto)
        ));
    }

    private Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }
}
