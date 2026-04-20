package com.tic_tac_toe.domain.service.game;

import com.tic_tac_toe.domain.model.Game;

import java.util.List;
import java.util.UUID;

public interface GameQueryService {
    Game getGameById(UUID gameId);

    List<Game> getGames();

    List<Game> getAvailableGames();

    List<Game> getFinishedGames(UUID userId);
}
