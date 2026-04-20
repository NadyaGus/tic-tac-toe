package com.tic_tac_toe.domain.service.game;

public class GameUtils {
    private static final int EMPTY = 0;

    public int checkWinner(int[][] field) {
        for (int i = 0; i < 3; i++) {
            if (field[i][0] != EMPTY && field[i][0] == field[i][1] && field[i][1] == field[i][2]) return field[i][0];
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i] != EMPTY && field[0][i] == field[1][i] && field[1][i] == field[2][i]) return field[0][i];
        }

        if (field[0][0] != EMPTY && field[0][0] == field[1][1] && field[1][1] == field[2][2]) return field[0][0];
        if (field[0][2] != EMPTY && field[0][2] == field[1][1] && field[1][1] == field[2][0]) return field[0][2];

        return -1;
    }

    public boolean isDraw(int[][] field) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == EMPTY) return false;
            }
        }
        return true;
    }
}
