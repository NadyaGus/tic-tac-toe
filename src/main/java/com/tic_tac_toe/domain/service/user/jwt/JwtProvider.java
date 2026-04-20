package com.tic_tac_toe.domain.service.user.jwt;

import com.tic_tac_toe.domain.model.User;
import io.jsonwebtoken.Claims;

public interface JwtProvider {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    Claims getClaims(String token);
}
