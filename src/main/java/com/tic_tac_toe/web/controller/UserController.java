package com.tic_tac_toe.web.controller;

import com.tic_tac_toe.domain.model.LeaderboardEntry;
import com.tic_tac_toe.domain.model.User;
import com.tic_tac_toe.domain.service.user.UserService;
import com.tic_tac_toe.web.mapper.UserMapper;
import com.tic_tac_toe.web.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getUserById(@PathVariable UUID uuid) {
        try {
            User user = userService.getUserById(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(userMapper.toWeb(user));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Cannot find the requested resource", e.getMessage()));
        }
    }

    @GetMapping("/user/top")
    public ResponseEntity<?> getTopUsers(@RequestParam(defaultValue = "5") int n) {
        try{
            List<LeaderboardEntry> leaders = userService.getTopPlayers(n);
            return new ResponseEntity<>(leaders, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Cannot retrieve top users", e.getMessage()));
        }
    }
}
