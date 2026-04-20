package com.tic_tac_toe.domain.service.game;

import com.tic_tac_toe.domain.model.Game;
import com.tic_tac_toe.domain.model.User;

import java.util.UUID;

public interface GameCommandService {
    Game createNewGameWithComputer(UUID playerId);

    Game createNewGameWithUser(UUID userId);

    void saveGame(Game game);

    void assignPlayer(User user, Game game);

    Game nextMove(Game game, int[][] field);

    void checkGameFinish(Game game);

    void validateGame(Game game, int[][] field, UUID userId);
}
