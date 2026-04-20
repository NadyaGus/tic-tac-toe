package com.tic_tac_toe.domain.service.user;

import com.tic_tac_toe.datasource.mapper.UserEntityMapper;
import com.tic_tac_toe.datasource.model.UserEntity;
import com.tic_tac_toe.datasource.repository.UserRepository;
import com.tic_tac_toe.domain.model.LeaderboardEntry;
import com.tic_tac_toe.domain.model.Role;
import com.tic_tac_toe.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final PasswordEncoder passwordEncoder;
    UserRepository userRepo;
    UserEntityMapper mapper;

    public UserService(UserRepository repo, UserEntityMapper mapper, PasswordEncoder passwordEncoder) {
        userRepo = repo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    User createUser(String username, String password) {
        if (userRepo.findUserByName(username) != null) {
            throw new IllegalArgumentException("Such username is already taken");
        }

        UUID id = UUID.randomUUID();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(id, username, Role.USER);
        userRepo.save(mapper.toData(user, encodedPassword));
        return user;
    }

    public User authenticate(String username, String rawPassword) {
        UserEntity entity = userRepo.findUserByName(username);
        if (entity == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (!passwordEncoder.matches(rawPassword, entity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return mapper.toDomain(entity);
    }

    public User getUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        UserEntity user = userRepo.findById(userId).orElse(null);
        if (user == null) throw new IllegalArgumentException("Can't find user with such ID: " + userId);
        return mapper.toDomain(user);
    }

    public List<LeaderboardEntry> getTopPlayers(int N) {
        List<Object[]> results = userRepo.findTopPlayers(N);
        List<LeaderboardEntry> leaders = new ArrayList<>();
        for (Object[] row : results) {
            UUID userId = (UUID) row[0];
            double winRatio = ((Number) row[1]).doubleValue();
            leaders.add(new LeaderboardEntry(userId, winRatio));
        }
        return leaders;
    }
}
