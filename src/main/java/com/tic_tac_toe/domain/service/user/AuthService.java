package com.tic_tac_toe.domain.service.user;

import com.tic_tac_toe.domain.model.JwtAuthentication;
import com.tic_tac_toe.web.model.user.JwtRequest;
import com.tic_tac_toe.web.model.user.JwtResponse;
import com.tic_tac_toe.web.model.user.SignUpRequest;

import java.util.UUID;

public interface AuthService {
    UUID registerUser(SignUpRequest request);

    JwtResponse authorizeUser(JwtRequest request);

    JwtResponse refreshAccessToken(String refreshToken);

    JwtResponse refreshRefreshToken(String refreshToken);

    JwtAuthentication getJwtAuthentication();

    void validateSignUpRequest(SignUpRequest request);
}
