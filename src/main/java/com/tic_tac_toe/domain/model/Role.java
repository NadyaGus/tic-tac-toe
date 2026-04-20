package com.tic_tac_toe.domain.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        // Spring Security ожидает префикс ROLE_ для методов hasRole()
        return "ROLE_" + name();
    }
}
