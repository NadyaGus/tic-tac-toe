package com.tic_tac_toe.domain.service.game;

import com.tic_tac_toe.datasource.mapper.GameEntityMapper;
import com.tic_tac_toe.datasource.model.GameEntity;
import com.tic_tac_toe.datasource.repository.GameRepository;
import com.tic_tac_toe.domain.model.Game;
import com.tic_tac_toe.domain.model.GameMode;
import com.tic_tac_toe.domain.model.GameStatus;
import com.tic_tac_toe.domain.model.User;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameService implements GameQueryService, GameCommandService{
    private static final int EMPTY = 0;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;
    private final GameUtils gameUtils;

    private final GameRepository repo;
    private final GameEntityMapper gameEntityMapper;
    private final MiniMax minimax;

    public GameService(GameRepository repo, GameEntityMapper gameEntityMapper, MiniMax minimax) {
        this.repo = repo;
        this.gameEntityMapper = gameEntityMapper;
        this.minimax = minimax;
        this.gameUtils = new GameUtils();
    }

    @Override
    public Game createNewGameWithComputer(UUID playerId) {
        Game game = new Game(UUID.randomUUID(), GameMode.AI, playerId);
        game.setInProgressGameStatus();
        game.setCurrentMove(playerId);
        saveGame(game);
        return game;
    }

    @Override
    public Game createNewGameWithUser(UUID playerId) {
        Game game = new Game(UUID.randomUUID(), GameMode.PVP, playerId);
        saveGame(game);
        return game;
    }

    @Override
    public void saveGame(Game game) {
        GameEntity entity = gameEntityMapper.toData(game);
        repo.save(entity);
    }

    @Override
    public Game getGameById(UUID gameId) {
        Game game = gameEntityMapper.toDomain(repo.findById(gameId).orElse(null));
        if (game == null) throw new IllegalArgumentException("Cannot find game with such ID");
        return game;
    }

    public List<Game> getGames() {
        Iterable<GameEntity> dataList = repo.findAll();
        ArrayList<Game> games = new ArrayList<>();
        dataList.forEach(g -> games.add(gameEntityMapper.toDomain(g)));
        return games;
    }

    public List<Game> getAvailableGames() {
        Iterable<GameEntity> dataList = repo.findAll();
        ArrayList<Game> games = new ArrayList<>();
        dataList.forEach(g -> games.add(gameEntityMapper.toDomain(g)));
        games.removeIf(g -> g.getStatus() != GameStatus.WAITING);
        return games;
    }

    public List<Game> getFinishedGames(UUID id) {
        Iterable<GameEntity> dataList = repo.findAllFinishedGames(id);
        ArrayList<Game> games = new ArrayList<>();
        dataList.forEach(g -> games.add(gameEntityMapper.toDomain(g)));
        return games;
    }

    @Override
    public void validateGame(Game game, int[][] field, UUID userId) {
        validateGameStatus(game);
        validateUser(game, userId);
        validateField(game, field, userId);
    }

    private void validateGameStatus(Game game) {
        if (game.getStatus() == GameStatus.WAITING)
            throw new IllegalStateException("Waiting opponent");
        if (game.getStatus() == GameStatus.WIN || game.getStatus() == GameStatus.DRAW)
            throw new IllegalStateException("Game is already finished");
    }

    private void validateUser(Game game, UUID userId) {
        if (!(Objects.equals(game.getPlayerX(), userId) || Objects.equals(game.getPlayerO(), userId))) {
            throw new IllegalStateException("User doesn't play in the current match");
        }

        if (!Objects.equals(game.getCurrentMove(), userId)) {
            throw new IllegalStateException("It's another players turn");
        }
    }

    private void validateField(Game oldGame, int[][] field, UUID userId) {
        int[][] oldField = oldGame.getFieldMatrix();

        boolean changed = false;
        boolean result = true;

        for (int i = 0; i < oldField.length && result; i++) {
            for (int j = 0; j < oldField[i].length; j++) {
                int oldValue = oldField[i][j];
                int newValue = field[i][j];
                if (newValue != EMPTY && newValue != PLAYER_X && newValue != PLAYER_O) {
                    result = false;
                    break;
                }
                if (oldValue != EMPTY && oldValue != newValue) {
                    result = false;
                    break;
                }
                if (oldValue != newValue) {
                    if (changed) {
                        result = false;
                        break;
                    }
                    changed = true;

                    if (oldGame.getMode() == GameMode.AI && newValue == PLAYER_O) {
                        result = false;
                        break;
                    }
                    if (oldGame.getMode() == GameMode.PVP) {
                        int currentUserSing =
                                Objects.equals(oldGame.getCurrentMove(), oldGame.getPlayerX()) ? PLAYER_X : PLAYER_O;
                        if (currentUserSing != newValue) {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }
        if (!result) throw new IllegalStateException("Invalid input field. Field must contain only '0', '1' or '2'." +
                "\n" + "Player can move once and only correct symbol");

        if (!changed) throw new IllegalStateException("Player did not change any cell");
    }

    @Override
    public Game nextMove(Game game, int[][] field) {
        if (game.getMode() == GameMode.AI) {
            return nextComputerMove(game, field);
        }

        game.setField(field);
        checkGameFinish(game);

        UUID lastMove = game.getCurrentMove();
        game.setCurrentMove(Objects.equals(lastMove, game.getPlayerX()) ?
                game.getPlayerO() : game.getPlayerX());

        return game;
    }

    private Game nextComputerMove(Game game, int[][] field) {
        int[] move = minimax.getBestMove(field);
        if (move != null) {
            field[move[0]][move[1]] = PLAYER_O;
        }

        game.getField().setField(field);
        checkGameFinish(game);

        return game;
    }

    @Override
    public void checkGameFinish(Game game) {
        if (game.getStatus() != GameStatus.WIN && game.getStatus() != GameStatus.DRAW) {
            int result = gameUtils.checkWinner(game.getFieldMatrix());
            if (result == PLAYER_X) {
                game.setWinner(game.getPlayerX().toString());
                game.setWinningGameStatus();
            } else if (result == PLAYER_O) {
                game.setWinner(game.getPlayerO() != null ? game.getPlayerO().toString() : "AI");
                game.setWinningGameStatus();
            } else if (result == -1 && gameUtils.isDraw(game.getFieldMatrix())) {
                game.setWinner("Draw");
                game.setDrawGameStatus();
            }
        }
    }

    @Transactional
    public void assignPlayer(User user, Game game) {
        game.addPlayer(user);
        game.setInProgressGameStatus();
        game.setCurrentMove(game.getPlayerX());
        repo.save(gameEntityMapper.toData(game));
    }

}
