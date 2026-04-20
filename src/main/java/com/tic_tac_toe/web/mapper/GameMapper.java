package com.tic_tac_toe.web.mapper;

import com.tic_tac_toe.domain.model.Game;
import com.tic_tac_toe.web.model.game.GameMode;
import com.tic_tac_toe.web.model.game.GameResponse;
import com.tic_tac_toe.web.model.game.GameStatus;

public class GameMapper {
    public GameStatus mapGameStatus(com.tic_tac_toe.domain.model.GameStatus status) {
        return switch (status) {
            case WAITING -> GameStatus.WAITING;
            case WIN -> GameStatus.WIN;
            case DRAW -> GameStatus.DRAW;
            case IN_PROGRESS -> GameStatus.IN_PROGRESS;
        };
    }

    public com.tic_tac_toe.domain.model.GameStatus mapGameStatus(GameStatus status) {
        return switch (status) {
            case WAITING -> com.tic_tac_toe.domain.model.GameStatus.WAITING;
            case WIN -> com.tic_tac_toe.domain.model.GameStatus.WIN;
            case DRAW -> com.tic_tac_toe.domain.model.GameStatus.DRAW;
            case IN_PROGRESS -> com.tic_tac_toe.domain.model.GameStatus.IN_PROGRESS;
        };
    }

    public GameMode mapGameMode(com.tic_tac_toe.domain.model.GameMode mode) {
        return switch (mode) {
            case AI -> GameMode.AI;
            case PVP -> GameMode.PVP;
        };
    }

    public com.tic_tac_toe.domain.model.GameMode mapGameMode(GameMode mode) {
        return switch (mode) {
            case AI -> com.tic_tac_toe.domain.model.GameMode.AI;
            case PVP -> com.tic_tac_toe.domain.model.GameMode.PVP;
        };
    }

    public GameResponse toWeb(Game game) {
        return new GameResponse(game.getId(), mapGameMode(game.getMode()), game.getCreatedAt(), game.getPlayerX(), game.getPlayerO(),
                mapGameStatus(game.getStatus()),
                game.getCurrentMove(), game.getFieldMatrix(), game.getWinner());
    }
}
