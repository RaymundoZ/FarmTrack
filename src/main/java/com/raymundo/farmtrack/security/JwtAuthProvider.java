package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.exception.AuthException;
import com.raymundo.farmtrack.exception.NotFoundException;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthToken token = (JwtAuthToken) authentication;
        String accessToken = (String) token.getPrincipal();
        String refreshToken = (String) token.getCredentials();
        if (jwtService.isTokenValid(accessToken)) {
            UUID userId = jwtService.getUserId(accessToken);
            TokenType type = jwtService.getTokenType(accessToken);
            UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                    NotFoundException.Code.USER_NOT_FOUND.get(userId.toString()));
            return new JwtAuthToken(user, type);
        } else if (jwtService.isTokenValid(refreshToken)) {
            UUID userId = jwtService.getUserId(refreshToken);
            TokenType type = jwtService.getTokenType(refreshToken);
            UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                    NotFoundException.Code.USER_NOT_FOUND.get(userId.toString()));
            return new JwtAuthToken(user, type);
        }
        throw AuthException.Code.TOKENS_EXPIRED.get();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthToken.class);
    }
}
