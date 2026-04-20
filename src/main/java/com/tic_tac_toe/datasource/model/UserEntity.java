package com.tic_tac_toe.datasource.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserEntity {
    @Id
    private final UUID id;
    private final String username;
    private final String password;

    @Enumerated(EnumType.STRING)
    private final Role role;

    public UserEntity(UUID id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Default constructor required by Hibernate
    private UserEntity() {
        this.id = null;
        this.username = null;
        this.password = null;
        this.role = null;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
