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

/**
 * Implementation of the {@link JwtService} interface for managing JWT tokens.
 * <p>
 * This service provides methods for generating, validating, and extracting information from JWT tokens.
 * It utilizes a secret key for signing tokens, which is retrieved from the application properties.
 *
 * @author RaymundoZ
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value(value = "${jwt-service.secret-key}")
    private String secretKey;

    /**
     * Generates a JWT token for the given user and token type.
     * <p>
     * This method generates a JWT token containing claims such as user ID, token type, issued
     * timestamp, and expiration timestamp. The expiration timestamp is determined based on
     * the token type. If the token type is ACCESS, the token expires in 10 minutes (600 seconds);
     * otherwise, if the token type is REFRESH, the token expires in 7 days (604800 seconds).
     * The JWT token is signed using the signing key obtained from {@code getSigningKey()} method.
     *
     * @param user      The user entity for whom the token is generated.
     * @param tokenType The type of token to be generated (ACCESS or REFRESH).
     * @return A JWT token as a {@code String}.
     */
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

    /**
     * Checks whether a given JWT token is valid.
     * <p>
     * This method attempts to parse and verify the given JWT token using the signing key
     * obtained from {@code getSigningKey()}. If the token is successfully parsed and verified,
     * the method returns true indicating that the token is valid; otherwise, if an exception
     * occurs during parsing or verification, or if the token is invalid, the method returns false.
     *
     * @param token The JWT token to be validated.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
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

    /**
     * Retrieves the user ID from a JWT token.
     * <p>
     * This method parses the provided JWT token, verifies it using the signing key obtained
     * from {@code getSigningKey()}, and retrieves the user ID from the token's payload.
     * The user ID is then returned as a {@link UUID}.
     *
     * @param token The JWT token from which to retrieve the user ID.
     * @return The user ID extracted from the JWT token as a {@link UUID}.
     * @throws JwtException             Thrown if an error occurs during parsing or verification of the token.
     * @throws IllegalArgumentException Thrown if the token is invalid or malformed.
     */
    @Override
    public UUID getUserId(String token) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("user_id", String.class));
    }

    /**
     * Retrieves the token type from a JWT token.
     * <p>
     * This method parses the provided JWT token, verifies it using the signing key obtained
     * from {@code getSigningKey()}, and retrieves the token type from the token's payload.
     * The token type is then returned as a {@link TokenType}.
     *
     * @param token The JWT token from which to retrieve the token type.
     * @return The token type extracted from the JWT token as a {@link TokenType}.
     * @throws JwtException             Thrown if an error occurs during parsing or verification of the token.
     * @throws IllegalArgumentException Thrown if the token is invalid or malformed.
     */
    @Override
    public TokenType getTokenType(String token) {
        return TokenType.valueOf(Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("token_type", String.class));
    }

    /**
     * Retrieves the signing key for JWT token generation.
     * <p>
     * This method converts the secret key string to bytes and uses it to generate
     * an HMAC SHA key using the Keys class from the JJWT library. The generated
     * key is then returned as a {@link SecretKey}.
     *
     * @return The signing key for JWT token generation.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
