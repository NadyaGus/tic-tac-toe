package com.tic_tac_toe.web.mapper;

import com.tic_tac_toe.domain.model.User;
import com.tic_tac_toe.web.model.game.Role;
import com.tic_tac_toe.web.model.game.UserResponse;

public class UserMapper {
    private com.tic_tac_toe.domain.model.Role mapUserRole(Role role) {
        return switch (role) {
            case USER -> com.tic_tac_toe.domain.model.Role.USER;
        };
    }

    private Role mapUserRole(com.tic_tac_toe.domain.model.Role role) {
        return switch (role) {
            case USER -> Role.USER;
        };
    }

    public UserResponse toWeb(User user){
        return new UserResponse(user.getId(), user.getUsername(), mapUserRole(user.getRole()));
    }

    public User toDomain(UserResponse response){
        return new User(response.getId(), response.getUsername(), mapUserRole(response.getRole()));
    }
}