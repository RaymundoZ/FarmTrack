package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.TokenType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "3a775fac9ecf5f8f7871381ff9ca563ad83098548e9524de4051cc7e1f85b9b3ee701dba10168e81f5006998c1d58b8ad608732469e9e6d31263605481033ba7a5a5022a42fcbf4cab6f2291cb18d8d92015c8c0ecacc75d288c66a3d9a80aaffc85de9102f2a918ba721564bfbeb25a4201cdeb941a59ccbe0949abbb8760a27e3fb62f97524f43e9b06355b1ac8bb8ca546c0bea70b505ffbea11dec1f72c32114f4eda4a2ae2d084937a284c5cc9cecedd5d48ac9bfc47349e6f9c207cc6be6a89a43470ead5b170cf00ce0132e7b925ed657a4ffa768bcfad72b721071074678c3f2a23d49faa36a60fefd06a34d550583795ec95b7935835c5a1a122c07bde8d570a6d3bd34105a5856fa8d8d024b5c46dd7be8d691b92ce01c1b615a5dcb68499fe249c3b65f57e2fa3727075f5b5fdc77c301857f9f6da2715d3dfc4771eb90c23bd2d2eccfeeeaf19087fb28d270b736c9f28489f24e5076615da0ede7091c295fc6942aa47e98240f839cea51cd4705f68077d13ee32e5cb89a38bbc86a963584f222182b66314fe17ed70eac233e7a59ef84b7473c167beb48d7757e1c76d28959e52f227f47d4ec4c9c22470774d350f745a0adf27a57beb62f046ff9f83225f968045edf24519ccbf6284923c81986a74a96fd7c514ac621137b66eda1ea97d11aaa27a44f2e3b091d8a999e06ca36862f42105ceea235886ae8";

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
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
