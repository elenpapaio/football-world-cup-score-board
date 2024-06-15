package com.sportsdatacompany;

import com.sportsdatacompany.data.InMemoryDatabase;
import com.sportsdatacompany.dto.GameDto;
import com.sportsdatacompany.model.Game;
import com.sportsdatacompany.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
    @DisplayName("deleteGameById - It should delete the game with the given id when it exists in the database")
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

        inMemoryDatabase.getGames().addAll(new ArrayList<>(Arrays.asList(game1, game2)));

        Game deletedGame = inMemoryDatabase.deleteGameById(2);

        assertEquals(deletedGame, game2);
        assertEquals(inMemoryDatabase.getGames().size(), 1);
    }

    @Test
    @DisplayName("deleteGameById - It should return null when the game with the given id does not exist in the database")
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

        inMemoryDatabase.getGames().add(game);

        Game deletedGame = inMemoryDatabase.deleteGameById(4);

        assertNull(deletedGame);
        assertEquals(inMemoryDatabase.getGames().size(), 1);
    }

    @Test
    @DisplayName("findGameById - It should find the game with the given id when it exists in the database")
    public void findGameById_test1() {
        Game game = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .homeTeamScore(1)
                .awayTeamScore(0)
                .build();

        inMemoryDatabase.getGames().add(game);

        Optional<Game> retrievedGameOptional = inMemoryDatabase.findGameById(1);
        Game retrievedGame = retrievedGameOptional.orElse(null);

        assertEquals(retrievedGame, game);
    }

    @Test
    @DisplayName("findGameById - It should return an empty optional when the given game id does not exist in the database")
    public void findGameById_test2() {
        Game game = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .homeTeamScore(1)
                .awayTeamScore(0)
                .build();

        inMemoryDatabase.getGames().add(game);

        Optional<Game> retrievedGameOptional = inMemoryDatabase.findGameById(5);

        assertEquals(retrievedGameOptional, Optional.empty());
    }

    @Test
    @DisplayName("updateGame - It should update the game with the given id when it exists in the database")
    public void updateGame_test1() {
        Game game = Game.builder()
                .gameId(2)
                .homeTeam(Team.builder()
                        .name("Spain")
                        .build())
                .awayTeam(Team.builder()
                        .name("Brazil")
                        .build())
                .homeTeamScore(0)
                .awayTeamScore(0)
                .build();

        inMemoryDatabase.getGames().add(game);

        Game updatedGame = inMemoryDatabase.updateGame(GameDto.builder()
                .gameId(2)
                .homeTeamScore(1)
                .awayTeamScore(1)
                .build());

        assertThat(updatedGame)
                .usingRecursiveComparison()
                .ignoringFields("homeTeamScore", "awayTeamScore")
                .isEqualTo(game);
        assertEquals(updatedGame.getHomeTeamScore(), 1);
        assertEquals(updatedGame.getAwayTeamScore(), 1);
    }

    @Test
    @DisplayName("updateGame - It should return null when the game with the given id does not exist in the database")
    public void updateGame_test2() {
        Game game = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder()
                        .name("Italy")
                        .build())
                .awayTeam(Team.builder()
                        .name("Uruguay")
                        .build())
                .homeTeamScore(1)
                .awayTeamScore(0)
                .build();

        inMemoryDatabase.getGames().add(game);

        Game updatedGame = inMemoryDatabase.updateGame(GameDto.builder()
                .gameId(2)
                .homeTeamScore(1)
                .awayTeamScore(1)
                .build());

        assertNull(updatedGame);
    }

}
