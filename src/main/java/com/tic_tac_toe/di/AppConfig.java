package com.tic_tac_toe.di;

import com.tic_tac_toe.datasource.mapper.GameEntityMapper;

import com.tic_tac_toe.datasource.mapper.UserEntityMapper;
import com.tic_tac_toe.datasource.repository.GameRepository;
import com.tic_tac_toe.datasource.repository.UserRepository;
import com.tic_tac_toe.domain.service.game.GameService;
import com.tic_tac_toe.domain.service.game.MiniMax;
import com.tic_tac_toe.domain.service.user.AuthService;
import com.tic_tac_toe.domain.service.user.AuthServiceImpl;
import com.tic_tac_toe.domain.service.user.UserService;
import com.tic_tac_toe.domain.service.user.jwt.JwtProvider;
import com.tic_tac_toe.domain.service.user.jwt.JwtUtil;
import com.tic_tac_toe.domain.service.user.jwt.RefreshTokenBlacklist;
import com.tic_tac_toe.web.controller.GameController;
import com.tic_tac_toe.web.mapper.GameMapper;
import com.tic_tac_toe.web.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public AuthService authService(UserService userService, JwtProvider provider,
                                   JwtUtil util, RefreshTokenBlacklist blackList) {
        return new AuthServiceImpl(userService, provider, util, blackList);
    }

    @Bean
    public UserService userService(UserRepository repo, UserEntityMapper mapper, PasswordEncoder encoder) {
        return new UserService(repo, mapper, encoder);
    }

    @Bean
    public GameService gameService(GameRepository repo, GameEntityMapper gameEntityMapper, MiniMax minimax) {
        return new GameService(repo, gameEntityMapper, minimax);
    }

    @Bean
    GameMapper webMapper() {
        return new GameMapper();
    }

    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

    @Bean
    public GameController gameController(GameService gameService, UserService userService, AuthService auth, GameMapper webMapper) {
        return new GameController(gameService, userService, auth, webMapper);
    }
}

