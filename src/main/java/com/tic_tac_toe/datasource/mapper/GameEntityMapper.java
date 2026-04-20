package com.tic_tac_toe.datasource.mapper;

import com.tic_tac_toe.datasource.model.GameEntity;
import com.tic_tac_toe.datasource.model.GameMode;
import com.tic_tac_toe.datasource.model.GameStatus;
import com.tic_tac_toe.domain.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameEntityMapper {
    private GameStatus mapGameStatus(com.tic_tac_toe.domain.model.GameStatus status) {
        return switch (status) {
            case WAITING -> GameStatus.WAITING;
            case WIN -> GameStatus.WIN;
            case DRAW -> GameStatus.DRAW;
            case IN_PROGRESS -> GameStatus.IN_PROGRESS;
        };
    }

    private com.tic_tac_toe.domain.model.GameStatus mapGameStatus(GameStatus status) {
        return switch (status) {
            case WAITING -> com.tic_tac_toe.domain.model.GameStatus.WAITING;
            case WIN -> com.tic_tac_toe.domain.model.GameStatus.WIN;
            case DRAW -> com.tic_tac_toe.domain.model.GameStatus.DRAW;
            case IN_PROGRESS -> com.tic_tac_toe.domain.model.GameStatus.IN_PROGRESS;
        };
    }

    private GameMode mapGameMode(com.tic_tac_toe.domain.model.GameMode mode) {
        return switch (mode) {
            case AI -> GameMode.AI;
            case PVP -> GameMode.PVP;
        };
    }

    private com.tic_tac_toe.domain.model.GameMode mapGameMode(GameMode mode) {
        return switch (mode) {
            case AI -> com.tic_tac_toe.domain.model.GameMode.AI;
            case PVP -> com.tic_tac_toe.domain.model.GameMode.PVP;
        };
    }

    public GameEntity toData(Game game) {
        return new GameEntity(game.getId(), game.getCreatedAt(), mapGameMode(game.getMode()), game.getPlayerX(), game.getPlayerO(),
                mapGameStatus(game.getStatus()),
                game.getCurrentMove(), game.getFieldMatrix(), game.getWinner());
    }

    public Game toDomain(GameEntity entity) {
        if (entity == null) return null;
        return new Game(entity.getId(), mapGameMode(entity.getMode()), entity.getCreatedAt(), entity.getPlayerX(), entity.getPlayerO(),
                mapGameStatus(entity.getStatus()),
                entity.getCurrentMove(), entity.getFieldMatrix(), entity.getWinner());
    }
}
