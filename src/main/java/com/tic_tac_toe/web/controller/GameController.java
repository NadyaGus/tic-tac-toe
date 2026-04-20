package com.tic_tac_toe.web.controller;

import com.tic_tac_toe.domain.model.Game;
import com.tic_tac_toe.domain.model.User;
import com.tic_tac_toe.domain.service.game.GameService;
import com.tic_tac_toe.domain.service.user.AuthService;
import com.tic_tac_toe.domain.service.user.UserService;
import com.tic_tac_toe.web.mapper.GameMapper;
import com.tic_tac_toe.web.model.ErrorResponse;
import com.tic_tac_toe.web.model.game.FieldRequest;
import com.tic_tac_toe.web.model.game.GameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class GameController {

    private final GameService gameService;
    private final GameMapper webMapper;
    private final UserService userService;
    private final AuthService auth;

    public GameController(GameService gameService, UserService userService, AuthService auth, GameMapper webMapper) {
        this.gameService = gameService;
        this.userService = userService;
        this.auth = auth;
        this.webMapper = webMapper;
    }

    private UUID validateAuth() {
        return (UUID) auth.getJwtAuthentication().getPrincipal();
    }

    @GetMapping("/game/{uuid}")
    public ResponseEntity<?> getGameById(@PathVariable UUID uuid) {
        try {
            Game game = gameService.getGameById(uuid);
            return ResponseEntity.ok(webMapper.toWeb(game));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error getting a game by id", e.getMessage()));
        }
    }

    @GetMapping("/game/available")
    public ResponseEntity<?> getAvailableGames() {
        try {
            Iterable<Game> games = gameService.getAvailableGames();
            ArrayList<GameResponse> webGames = new ArrayList<>();
            games.forEach(g -> webGames.add(webMapper.toWeb(g)));
            return ResponseEntity.ok(webGames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/game/finished")
    public ResponseEntity<?> getFinishedGames() {
        try {
            var userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userId != null) {
                UUID currentUserId = UUID.fromString(userId.toString());
                Iterable<Game> finishedGames = gameService.getFinishedGames(currentUserId);
                ArrayList<GameResponse> webFinishedGames = new ArrayList<>();
                finishedGames.forEach(g -> webFinishedGames.add(webMapper.toWeb(g)));
                return ResponseEntity.ok(finishedGames);
            }

            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Bad Request",
                            "Requested parameter should equal to abailable/finised."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    @PostMapping("/game/new")
    public ResponseEntity<?> initGame(@RequestParam(defaultValue = "computer") String mode) {
        try {
            UUID userId = validateAuth();

            Game newGame = null;
            if ("computer".equalsIgnoreCase(mode)) {
                newGame = gameService.createNewGameWithComputer(userId);
            } else if ("human".equalsIgnoreCase(mode)) {
                newGame = gameService.createNewGameWithUser(userId);
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Bad Request", "Mode should equal to computer/human."));
            }
            GameResponse response = webMapper.toWeb(newGame);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    @PostMapping("/game/{uuid}")
    public ResponseEntity<?> makeMove(@PathVariable UUID uuid, @RequestBody FieldRequest request) {
        try {
            UUID userId = validateAuth();

            Game oldGame = gameService.getGameById(uuid); // IllegalArgument
            gameService.validateGame(oldGame, request.getField(), userId); // IllegalState

            GameResponse response = null;
            Game updatedGame = null;
            updatedGame = gameService.nextMove(oldGame, request.getField());

            gameService.saveGame(updatedGame);
            response = webMapper.toWeb(updatedGame);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Cannot find requested resource", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error updating a game", e.getMessage()));
        }

    }

    @PostMapping("/game/join/{gameId}")
    public ResponseEntity<?> joinToGame(@PathVariable UUID gameId) {
        UUID userId = validateAuth();

        try {
            User user = userService.getUserById(userId);
            Game game = gameService.getGameById(gameId);
            gameService.assignPlayer(user, game);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error joining the game", e.getMessage()));
        }
    }
}
