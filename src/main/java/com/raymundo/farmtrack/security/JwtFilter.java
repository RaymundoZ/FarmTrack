package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

import static com.raymundo.farmtrack.util.Constants.ACCESS_TOKEN;
import static com.raymundo.farmtrack.util.Constants.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final SecurityContextHolderStrategy holderStrategy;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final WebAuthenticationDetailsSource detailsSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().endsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie accessToken = WebUtils.getCookie(request, ACCESS_TOKEN);
        Cookie refreshToken = WebUtils.getCookie(request, REFRESH_TOKEN);
        JwtAuthToken authToken = new JwtAuthToken(accessToken == null ? null : accessToken.getValue(),
                refreshToken == null ? null : refreshToken.getValue());
        JwtAuthToken authentication;
        try {
            authentication = (JwtAuthToken) authManager.authenticate(authToken);
            authentication.setDetails(detailsSource.buildDetails(request));
        } catch (AuthenticationException e) {
            filterChain.doFilter(request, response);
            return;
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();
        TokenType type = (TokenType) authentication.getCredentials();
        if (type.equals(TokenType.REFRESH)) {
            response.addCookie(getCookie(ACCESS_TOKEN, null));
            response.addCookie(getCookie(REFRESH_TOKEN, null));
            response.addCookie(getCookie(ACCESS_TOKEN, jwtService.generateToken(user, TokenType.ACCESS)));
            response.addCookie(getCookie(REFRESH_TOKEN, jwtService.generateToken(user, TokenType.REFRESH)));
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
