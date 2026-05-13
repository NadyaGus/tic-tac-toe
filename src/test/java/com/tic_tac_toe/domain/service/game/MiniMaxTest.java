package com.tic_tac_toe.domain.service.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiniMaxTest {

    private final MiniMax minimax = new MiniMax();

    @Test
    void getBestMove_shouldTakeWinningMoveForO_horizontal() {
        int[][] field = {
                {2, 2, 0},
                {1, 0, 0},
                {0, 0, 1}
        };
        int[] expectedMove = {0, 2};
        int[] actualMove = minimax.getBestMove(field);
        assertNotNull(actualMove);
        assertArrayEquals(expectedMove, actualMove);
    }

    @Test
    void getBestMove_shouldBlockWinningMoveForX() {
        int[][] field = {
                {1, 2, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        int[] expectedMove = {2, 2};
        int[] actualMove = minimax.getBestMove(field);
        assertNotNull(actualMove);
        assertArrayEquals(expectedMove, actualMove);
    }

    @Test
    void getBestMove_shouldReturnSomeMove_whenBoardEmpty() {
        int[][] field = new int[3][3];
        int[] actualMove = minimax.getBestMove(field);
        assertNotNull(actualMove);
        assertTrue(actualMove[0] >= 0 && actualMove[0] < 3);
        assertTrue(actualMove[1] >= 0 && actualMove[1] < 3);
    }

    @Test
    void getBestMove_shouldPreferWinningOverBlocking() {
        int[][] field = {
                {2, 1, 0},
                {2, 1, 0},
                {0, 0, 0}
        };
        int[] expectedMove = {2, 0};
        int[] actualMove = minimax.getBestMove(field);
        assertNotNull(actualMove);
        assertArrayEquals(expectedMove, actualMove);
    }
}