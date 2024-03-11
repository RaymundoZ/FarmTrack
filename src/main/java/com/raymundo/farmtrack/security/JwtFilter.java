package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy holderStrategy;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().endsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie accessToken = WebUtils.getCookie(request, "access_token");
        Cookie refreshToken = WebUtils.getCookie(request, "refresh_token");
        JwtAuthToken authToken = new JwtAuthToken(accessToken == null ? null : accessToken.getValue(),
                refreshToken == null ? null : refreshToken.getValue());
        Authentication authentication;
        try {
            authentication = authManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            filterChain.doFilter(request, response);
            return;
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();
        TokenType type = (TokenType) authentication.getCredentials();
        if (type.equals(TokenType.REFRESH)) {
            response.addCookie(getCookie("access_token", null));
            response.addCookie(getCookie("refresh_token", null));
            response.addCookie(getCookie("access_token", jwtService.generateToken(user, TokenType.ACCESS)));
            response.addCookie(getCookie("refresh_token", jwtService.generateToken(user, TokenType.REFRESH)));
        }

        SecurityContext securityContext = holderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        holderStrategy.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

    private Cookie getCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }
}
