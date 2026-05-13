package com.tic_tac_toe.domain.service.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameUtilsTest {

    private final GameUtils gameUtils = new GameUtils();

    @Test
    void checkWinner_shouldReturnX() {
        int[][] field = {
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0}
        };
        assertEquals(1, gameUtils.checkWinner(field));
    }

    @Test
    void checkWinner_shouldReturnO() {
        int[][] field = {
                {0, 2, 0},
                {0, 2, 0},
                {0, 2, 0}
        };
        assertEquals(2, gameUtils.checkWinner(field));
    }

    @Test
    void checkWinner_shouldReturnDraw() {
        int[][] field = {
                {1, 2, 1},
                {1, 2, 2},
                {2, 1, 1}
        };
        assertEquals(-1, gameUtils.checkWinner(field));
    }

    @Test
    void checkWinner_shouldReturnX_whenMainDiagonalAllX() {
        int[][] field = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        assertEquals(1, gameUtils.checkWinner(field));
    }

    @Test
    void checkWinner_shouldReturnO_whenAntiDiagonalAllO() {
        int[][] field = {
                {0, 0, 2},
                {0, 2, 0},
                {2, 0, 0}
        };
        assertEquals(2, gameUtils.checkWinner(field));
    }

    @Test
    void isDraw_shouldReturnTrue_whenFullAndNoWinner() {
        int[][] field = {
                {1, 2, 1},
                {1, 2, 2},
                {2, 1, 2}
        };
        assertTrue(gameUtils.isDraw(field));
    }

    @Test
    void isDraw_shouldReturnFalse_whenNotFull() {
        int[][] field = {
                {1, 2, 0},
                {1, 2, 0},
                {0, 0, 0}
        };
        assertFalse(gameUtils.isDraw(field));
    }

    @Test
    void isDraw_shouldReturnFalse_whenFullButWinnerExists() {
        int[][] field = {
                {1, 1, 1},
                {2, 2, 0},
                {2, 0, 0}
        };
        assertFalse(gameUtils.isDraw(field));
    }
}