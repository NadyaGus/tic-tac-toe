package com.tic_tac_toe.web.model.user;

public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;

    public JwtResponse(String accessToken, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
