package com.raymundo.farmtrack.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raymundo.farmtrack.dto.basic.ErrorDto;
import com.raymundo.farmtrack.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorDto errorDto = new ErrorDto(
                HttpStatus.UNAUTHORIZED.value(),
                AuthException.Code.TOKENS_EXPIRED.get().getClass().getSimpleName(),
                AuthException.Code.TOKENS_EXPIRED.get().getMessage(),
                LocalTime.now()
        );
        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorDto));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
