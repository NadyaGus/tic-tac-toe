package com.tic_tac_toe.domain.model;

import java.util.UUID;

public class LeaderboardEntry {
    private final UUID userId;
    private final double winRate;

    public LeaderboardEntry(UUID id, double winRatio) {
        this.userId = id;
        winRate = winRatio;
    }

    public UUID getUserId() {
        return userId;
    }

    public double getWinRate() {
        return winRate;
    }
}
