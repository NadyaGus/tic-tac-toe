package com.tic_tac_toe.domain.service.user;

import com.tic_tac_toe.domain.model.User;
import com.tic_tac_toe.domain.service.user.jwt.JwtProvider;
import com.tic_tac_toe.domain.service.user.jwt.JwtUtil;
import com.tic_tac_toe.domain.service.user.jwt.RefreshTokenBlacklist;
import com.tic_tac_toe.domain.model.JwtAuthentication;
import com.tic_tac_toe.web.model.user.JwtRequest;
import com.tic_tac_toe.web.model.user.JwtResponse;
import com.tic_tac_toe.web.model.user.SignUpRequest;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;
    private final RefreshTokenBlacklist blacklist;

    public AuthServiceImpl(UserService userService, JwtProvider provider,
                           JwtUtil util, RefreshTokenBlacklist blacklist) {
        this.userService = userService;
        this.jwtProvider = provider;
        this.jwtUtil = util;
        this.blacklist = blacklist;
    }

    public UUID registerUser(SignUpRequest request) {
        User user = userService.createUser(request.getUsername(), request.getPassword());
        return user.getId();
    }

    @Override
    public JwtResponse authorizeUser(JwtRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username.isEmpty() && password.isEmpty()) {
            throw new IllegalArgumentException("Username and password is not provided");
        }

        try {
            User userByPassword = userService.authenticate(username, password);
            String accessToken = jwtProvider.generateAccessToken(userByPassword);
            String refreshToken = jwtProvider.generateRefreshToken(userByPassword);

            Claims claims = jwtProvider.getClaims(accessToken);
            JwtAuthentication auth = jwtUtil.createJwtAuthentication(claims);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return new JwtResponse(accessToken, refreshToken, "Bearer");
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("Can't create token");
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        if (blacklist.isBlacklisted(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has been revoked");
        }

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        User user = userService.getUserById(userId);

        String newAccessToken = jwtProvider.generateAccessToken(user);

        return new JwtResponse(newAccessToken, refreshToken, "Bearer");
    }

    @Override
    public JwtResponse refreshRefreshToken(String refreshToken) {
        if (blacklist.isBlacklisted(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has been revoked");
        }

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("userId", String.class));
        User user = userService.getUserById(userId);

        blacklist.add(refreshToken);

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse(newAccessToken, newRefreshToken, "Bearer");
    }

    public JwtAuthentication getJwtAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthentication jwtAuth) {
            return jwtAuth;
        }
        throw new RuntimeException("User not authenticated");
    }

    public void validateSignUpRequest(SignUpRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Invalid Request. Username and password are required");
        }
    }

    public void validateLoginRequest(JwtRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Invalid Request. Username and password are required");
        }
    }
}
