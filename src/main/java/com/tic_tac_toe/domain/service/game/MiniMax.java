package com.tic_tac_toe.domain.service.game;

import org.springframework.stereotype.Component;

@Component
public class MiniMax {
    private static final int EMPTY = 0;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;
    private static final int WIN_SCORE = 10;
    private static final int LOSS_SCORE = -10;
    private static final int DRAW_SCORE = 0;
    private final GameUtils gameUtils;

    public MiniMax() {
        this.gameUtils = new GameUtils();
    }

    public int[] getBestMove(int[][] field) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == EMPTY) {
                    field[i][j] = PLAYER_O;
                    int score = minimax(field, 0, false);
                    field[i][j] = EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int[][] field, int depth, boolean isMaximizing) {
        int winner = gameUtils.checkWinner(field);
        if (winner == PLAYER_O) return WIN_SCORE - depth;
        if (winner == PLAYER_X) return LOSS_SCORE + depth;
        if (gameUtils.isDraw(field)) return DRAW_SCORE;

        int best;
        if (isMaximizing) {
            best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == EMPTY) {
                        field[i][j] = PLAYER_O;
                        int score = minimax(field, depth + 1, false);
                        field[i][j] = EMPTY;
                        best = Math.max(score, best);
                    }
                }
            }
        } else {
            best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == EMPTY) {
                        field[i][j] = PLAYER_X;
                        int score = minimax(field, depth + 1, true);
                        field[i][j] = EMPTY;
                        best = Math.min(score, best);
                    }
                }
            }
        }
        return best;
    }
}