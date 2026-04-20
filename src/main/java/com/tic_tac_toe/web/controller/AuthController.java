package com.tic_tac_toe.web.controller;

import com.tic_tac_toe.domain.service.user.AuthService;
import com.tic_tac_toe.web.model.ErrorResponse;
import com.tic_tac_toe.web.model.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AuthController {
    AuthService auth;

    public AuthController(AuthService authService) {
        this.auth = authService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> createUser(@RequestBody SignUpRequest request) {
        try {
            auth.validateSignUpRequest(request);
            UUID id = auth.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Error creating new User", e.getMessage()));
        }

    }

    @PostMapping("/user/login")
    public ResponseEntity<?> authUser(@RequestBody JwtRequest request) {
        try {
            JwtResponse response = auth.authorizeUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Error authenticating user", e.getMessage()));
        }
    }

//    @PostMapping("/user/logout")

    @PostMapping("user/refresh/access")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshJwtRequest token) {
        try {
            JwtResponse response = auth.refreshAccessToken(token.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid Token", e.getMessage()));
        }
    }

    @PostMapping("user/refresh/refresh")
    public ResponseEntity<?> refreshRefreshToken(@RequestBody RefreshJwtRequest token) {
        try {
            JwtResponse response = auth.refreshRefreshToken(token.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid Token", e.getMessage()));
        }
    }
}