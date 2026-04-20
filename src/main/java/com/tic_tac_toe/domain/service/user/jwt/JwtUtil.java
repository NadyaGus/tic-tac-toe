package com.tic_tac_toe.domain.service.user.jwt;

import com.tic_tac_toe.domain.model.JwtAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtil {
    public JwtAuthentication createJwtAuthentication(Claims claims) {
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        String role = claims.get("role", String.class);
        return new JwtAuthentication(userId, role);
    }
}
