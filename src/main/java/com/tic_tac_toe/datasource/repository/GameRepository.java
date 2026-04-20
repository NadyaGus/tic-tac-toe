package com.tic_tac_toe.datasource.repository;

import com.tic_tac_toe.datasource.model.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {

    @Query("SELECT ge FROM GameEntity ge WHERE" +
            " (ge.status = com.tic_tac_toe.datasource.model.GameStatus.WIN" +
            " OR ge.status = com.tic_tac_toe.datasource.model.GameStatus.DRAW)" +
            " AND (ge.playerX = :userId OR ge.playerO = :userId)")
        Iterable<GameEntity> findAllFinishedGames(@Param("userId") UUID userId);
}
