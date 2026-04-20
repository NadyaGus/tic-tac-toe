package com.tic_tac_toe.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Game {
    private final UUID id;
    private final GameMode mode;
    private final String createdAt;
    private UUID playerX;
    private UUID playerO;
    private GameStatus status;
    private UUID currentMove;
    private final Field field;
    private String winner;

    public Game(UUID gameId, GameMode mode, UUID playerX) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss", new Locale("ru"));
        this.id = gameId;
        this.mode = mode;
        this.createdAt = now.format(formatter);
        this.playerX = playerX;
        this.playerO = null;
        status = GameStatus.WAITING;
        currentMove = null;
        this.field = new Field(new int[3][3]);
        this.winner = "";
    }

    public Game(UUID id, GameMode mode, String createdAt, UUID playerX, UUID playerO,
                GameStatus status, UUID move, int[][] field, String winner) {
        this.id = id;
        this.mode = mode;
        this.createdAt = createdAt;
        this.playerX = playerX;
        this.playerO = playerO;
        this.status = status;
        currentMove = move;
        this.field = new Field(field);
        this.winner = winner;
    }

    public void addPlayer(User user) {
        if (status != GameStatus.WAITING) {
            throw new IllegalStateException("Game has started");
        }

        if (playerX == null) {
            playerX = user.getId();
        } else {
            if (Objects.equals(playerX, user.getId())) {
                throw new IllegalArgumentException("User already in the game");
            }
            playerO = user.getId();
        }
    }

    public UUID getId() {
        return id;
    }

    public Field getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field.setField(field);
    }

    public int[][] getFieldMatrix() {
        return field.getField();
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public void setInProgressGameStatus() {
        this.status = GameStatus.IN_PROGRESS;
    }

    public void setDrawGameStatus() {
        this.status = GameStatus.DRAW;
    }

    public void setWinningGameStatus() {
        this.status = GameStatus.WIN;
    }

    public UUID getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(UUID currentMove) {
        this.currentMove = currentMove;
    }

    public GameMode getMode() {
        return mode;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
