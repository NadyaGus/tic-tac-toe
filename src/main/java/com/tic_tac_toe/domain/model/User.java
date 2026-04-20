package com.tic_tac_toe.domain.model;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final Role role;

    public User(UUID id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
