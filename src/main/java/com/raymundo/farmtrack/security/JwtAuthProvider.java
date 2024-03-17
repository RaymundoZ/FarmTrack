package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import com.raymundo.farmtrack.util.exception.AuthException;
import com.raymundo.farmtrack.util.exception.NotFoundException;
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
        if (jwtService.isTokenValid(accessToken))
            return getAuthentication(accessToken);
        else if (jwtService.isTokenValid(refreshToken))
            return getAuthentication(refreshToken);

        throw AuthException.Code.TOKENS_EXPIRED.get();
    }

    private Authentication getAuthentication(String token) throws AuthenticationException {
        UUID userId = jwtService.getUserId(token);
        TokenType type = jwtService.getTokenType(token);
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                NotFoundException.Code.USER_NOT_FOUND.get(userId.toString()));
        if (!user.isEnabled())
            throw AuthException.Code.ACCOUNT_BLOCKED.get();
        return new JwtAuthToken(user, type);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthToken.class);
    }
}
