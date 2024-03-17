package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @Value(value = "${jwt-service.secret-key}")
    private String secretKey;

    @Override
    public String generateToken(UserEntity user, TokenType tokenType) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusSeconds(tokenType.equals(TokenType.ACCESS) ? 600 : 604800);
        return Jwts.builder().claims()
                .add("user_id", user.getId().toString())
                .add("token_type", tokenType.toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public UUID getUserId(String token) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("user_id", String.class));
    }

    @Override
    public TokenType getTokenType(String token) {
        return TokenType.valueOf(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("token_type", String.class));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
