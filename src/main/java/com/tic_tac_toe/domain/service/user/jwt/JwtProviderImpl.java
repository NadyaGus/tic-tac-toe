package com.tic_tac_toe.domain.service.user.jwt;

import com.tic_tac_toe.domain.model.Role;
import com.tic_tac_toe.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtProviderImpl implements JwtProvider {
    private final SecretKey secretKey;
    private final RefreshTokenBlacklist blacklist;

    public JwtProviderImpl(RefreshTokenBlacklist refreshTokenBlackList,
                           @Value("${jwt.secret}") String secretKeyString) {
        blacklist = refreshTokenBlackList;
        secretKey = createSecretKey(secretKeyString);
    }

    private SecretKey createSecretKey(String secretKeyString) {
        // Попробуем интерпретировать как Base64, иначе как обычную строку
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            // Если не Base64, используем строку напрямую
            return Keys.hmacShaKeyFor(secretKeyString.getBytes());
        }
    }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30))) // 30 минут
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .claim("userId", user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))) // неделя
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // TODO add logging
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String token) {
        if (blacklist.isBlacklisted(token))
            return false;

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getUserIdFromClaims(Claims claims) {
        return UUID.fromString(claims.get("userId", String.class));
    }

    public Role getRoleFromClaims(Claims claims) {
        return Role.valueOf(claims.get("role", String.class));
    }
}
