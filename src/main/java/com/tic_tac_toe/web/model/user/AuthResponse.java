package com.tic_tac_toe.web.model.user;

import java.util.UUID;

public class AuthResponse {
    UUID uuid;

    public AuthResponse(UUID id) {
        this.uuid = id;
    }

    public UUID getUuid() {
        return uuid;
    }
}
