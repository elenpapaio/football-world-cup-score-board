package com.sportradar;

import com.sportradar.data.InMemoryDatabase;
import com.sportradar.model.Game;
import com.sportradar.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryDatabaseTest {

    private InMemoryDatabase inMemoryDatabase;

    @BeforeEach
    public void init() {
        inMemoryDatabase = new InMemoryDatabase();
    }

    @Test
    @DisplayName("insertGame - It should insert the provided game to the in memory database")
    public void insertGame() {
        Game game = Game.builder()
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .awayTeamScore(0)
                .homeTeamScore(0)
                .build();

        Game insertedGame = inMemoryDatabase.insertGame(game);

        Assertions.assertEquals(insertedGame.getGameId(), 1);
        assertThat(insertedGame)
                .usingRecursiveComparison()
                .ignoringFields("gameId")
                .isEqualTo(game);
    }

}
