package com.tic_tac_toe.datasource.repository;

import com.tic_tac_toe.datasource.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    @Query("SELECT ue FROM UserEntity ue WHERE ue.username = ?1 AND ue.password = ?2")
    UserEntity findUserByNameAndPassword(String username, String password);

    @Query("SELECT ue FROM UserEntity ue WHERE ue.username = ?1 LIMIT 1")
    UserEntity findUserByName(String username);

    @Query(value = """
            SELECT
                u.id AS user_id,
                ROUND(
                    (COUNT(CASE
                        WHEN (g.playero = u.id AND g.winner = u.id::text)
                          OR (g.playerx = u.id AND g.winner = u.id::text)
                        THEN 1
                    END) * 1.0 / COUNT(*))::numeric, 2
                ) AS win_ratio
            FROM user_entity u
            JOIN game_entity g ON (g.playero = u.id OR g.playerx = u.id)
            WHERE g.status IN (2, 3)
            GROUP BY u.id
            HAVING COUNT(*) > 0
            ORDER BY win_ratio DESC
            LIMIT :topN
            """, nativeQuery = true)
    List<Object[]> findTopPlayers(@Param("topN") int topN);
}
