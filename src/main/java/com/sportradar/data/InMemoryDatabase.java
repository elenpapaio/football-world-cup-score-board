package com.sportradar.data;

import com.sportradar.dto.GameDto;
import com.sportradar.model.Game;
import com.sportradar.model.Team;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryDatabase {
    @Getter
    private final List<Game> games = new ArrayList<>();
    private int newId = 1;

    public Game insertGame(Game game) {
        Game gameToBeInserted = Game.builder()
                .gameId(getNewId())
                .homeTeam(Team.builder()
                        .name(game.getHomeTeam()
                                .getName())
                        .build())
                .awayTeam(Team.builder()
                        .name(game.getAwayTeam()
                                .getName())
                        .build())
                .awayTeamScore(game.getAwayTeamScore())
                .homeTeamScore(game.getHomeTeamScore())
                .build();
        games.add(gameToBeInserted);
        return gameToBeInserted;
    }

    public Game deleteGameById(int gameId) {
        Optional<Game> gameToBeRemoved = games.stream()
                .filter(game -> game.getGameId() == gameId)
                .findFirst();
        gameToBeRemoved.ifPresent(games::remove);
        return gameToBeRemoved.orElse(null);
    }

    public Optional<Game> findGameById(int gameId) {
        throw new UnsupportedOperationException();
    }

    public Game updateGame(GameDto gameDto) {
        throw new UnsupportedOperationException();
    }

    private int getNewId() {
        int id = newId;
        newId = newId + 1;
        return id;
    }

}
