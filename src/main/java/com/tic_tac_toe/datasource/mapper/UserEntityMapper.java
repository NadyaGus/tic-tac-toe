package com.tic_tac_toe.datasource.mapper;

import com.tic_tac_toe.datasource.model.Role;
import com.tic_tac_toe.datasource.model.UserEntity;
import com.tic_tac_toe.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public Role mapRole(com.tic_tac_toe.domain.model.Role role){
        return switch(role){
            case USER -> Role.USER;
        };
    }

    public com.tic_tac_toe.domain.model.Role mapRole(Role role){
        return switch(role){
            case USER -> com.tic_tac_toe.domain.model.Role.USER;
        };
    }

    public UserEntity toData(User user, String password) {
        return new UserEntity(user.getId(), user.getUsername(), password, mapRole(user.getRole()));
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(entity.getId(), entity.getUsername(), mapRole(entity.getRole()));
    }
}
