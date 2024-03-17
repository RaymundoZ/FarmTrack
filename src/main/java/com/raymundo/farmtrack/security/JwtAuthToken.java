package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtAuthToken extends AbstractAuthenticationToken {

    private final String accessToken;
    private final String refreshToken;
    private final UserEntity user;
    private final TokenType type;

    public JwtAuthToken(String accessToken, String refreshToken) {
        super(List.of());
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = null;
        this.type = null;
        setAuthenticated(false);
    }

    public JwtAuthToken(UserEntity user, TokenType type) {
        super(List.of(new SimpleGrantedAuthority(user.getRole().toString())));
        this.user = user;
        this.type = type;
        this.accessToken = null;
        this.refreshToken = null;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return refreshToken != null ? refreshToken : type;
    }

    @Override
    public Object getPrincipal() {
        return accessToken != null ? accessToken : user;
    }
}
