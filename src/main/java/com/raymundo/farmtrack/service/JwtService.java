package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.util.TokenType;

import java.util.UUID;

public interface JwtService {

    String generateToken(UserEntity user, TokenType tokenType);

    boolean isTokenValid(String token);

    UUID getUserId(String token);

    TokenType getTokenType(String token);
}
