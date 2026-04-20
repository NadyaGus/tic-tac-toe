package com.tic_tac_toe.datasource.model;

import com.tic_tac_toe.datasource.mapper.FieldDataConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class GameEntity {
    @Id
    private final UUID id;
    private final String createdAt;
    private final GameMode mode;
    private final UUID playerX;
    private final UUID playerO;
    private final GameStatus status;
    private final UUID currentMove;
    @Convert(converter = FieldDataConverter.class)
    private final FieldEntity field;
    private final String winner;

    // Default constructor required by Hibernate
    private GameEntity() {
        this.id = null;
        this.createdAt = null;
        this.mode = null;
        this.playerX = null;
        this.playerO = null;
        this.status = null;
        this.currentMove = null;
        this.field = null;
        this.winner = null;
    }

    public GameEntity(UUID id, String createdAt, GameMode mode, UUID playerX, UUID playerO,
                      GameStatus status, UUID move, int[][] field, String winner) {
        this.id = id;
        this.createdAt = createdAt;
        this.mode = mode;
        this.playerX = playerX;
        this.playerO = playerO;
        this.status = status;
        this.currentMove = move;
        this.field = new FieldEntity(field);
        this.winner = winner;
    }

    public UUID getId() {
        return id;
    }

    public int[][] getFieldMatrix() {
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
