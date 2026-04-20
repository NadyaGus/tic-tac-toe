package com.tic_tac_toe.web.model.game;

import java.util.UUID;

public class GameResponse {
    private final UUID id;
    private final GameMode mode;
    private final String createdAt;
    private GameStatus status;
    private final UUID playerX;
    private final UUID playerO;
    private final UUID currentMove;
    private final FieldRequest field;
    private final String winner;

    public GameResponse(UUID id, GameMode mode, String createdAt, UUID playerX, UUID playerO,
                        GameStatus status, UUID move, int[][] field, String winner) {
        this.id = id;
        this.mode = mode;
        this.createdAt = createdAt;
        this.status = status;
        this.playerX = playerX;
        this.playerO = playerO;
        currentMove = move;
        this.field = new FieldRequest(field);
        this.winner = winner;
    }

    public UUID getId() {
        return id;
    }

    public int[][] getField() {
        return field.getField();
    }

    public String getWinner() {
        return winner;
    }

    public UUID getPlayerX() {
        return playerX;
    }

    public UUID getPlayerO() {
        return playerO;
    }

    public GameStatus getStatus() {
        return status;
    }

    public UUID getCurrentMove() {
        return currentMove;
    }

    public GameMode getMode() {
        return mode;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
