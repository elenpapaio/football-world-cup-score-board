package com.sportradar;

import com.sportradar.data.InMemoryDatabase;
import com.sportradar.model.Game;
import com.sportradar.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

        assertEquals(insertedGame.getGameId(), 1);
        assertThat(insertedGame)
                .usingRecursiveComparison()
                .ignoringFields("gameId")
                .isEqualTo(game);
    }

    @Test
    @DisplayName("deleteGameById - The game with the given id exists, so it should be deleted")
    public void deleteGameById_test1() {
        Game game1 = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .awayTeamScore(1)
                .homeTeamScore(0)
                .build();

        Game game2 = Game.builder()
                .gameId(2)
                .homeTeam(Team.builder()
                        .name("Spain")
                        .build())
                .awayTeam(Team.builder()
                        .name("Brazil")
                        .build())
                .awayTeamScore(0)
                .homeTeamScore(0)
                .build();

        inMemoryDatabase.getGames()
                .addAll(new ArrayList<>(Arrays.asList(game1, game2)));

        Game deletedGame = inMemoryDatabase.deleteGameById(2);

        assertEquals(deletedGame, game2);
        assertEquals(inMemoryDatabase.getGames().size(), 1);
    }

    @Test
    @DisplayName("deleteGameById - The game with the given id does not exist, so it should be return null")
    public void deleteGameById_test2() {
        Game game = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .awayTeamScore(1)
                .homeTeamScore(0)
                .build();

        inMemoryDatabase.getGames()
                .add(game);

        Game deletedGame = inMemoryDatabase.deleteGameById(4);

        assertNull(deletedGame);
        assertEquals(inMemoryDatabase.getGames().size(), 1);
    }

}
