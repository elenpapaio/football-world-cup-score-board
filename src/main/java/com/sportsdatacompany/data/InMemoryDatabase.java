package com.sportsdatacompany.data;

import com.sportsdatacompany.dto.GameDto;
import com.sportsdatacompany.model.Game;
import com.sportsdatacompany.model.Team;
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
        return games.stream()
                .filter(game -> game.getGameId() == gameId)
                .findFirst();
    }

    public Game updateGame(GameDto gameDto) {
        Optional<Game> gameToBeUpdated = games.stream()
                .filter(game -> game.getGameId() == gameDto.getGameId())
                .findFirst();
        gameToBeUpdated.ifPresent(game -> game.setHomeTeamScore(gameDto.getHomeTeamScore()));
        gameToBeUpdated.ifPresent(game -> game.setAwayTeamScore(gameDto.getAwayTeamScore()));
        return gameToBeUpdated.orElse(null);
    }

    public List<Game> findAllGames() {
        throw new UnsupportedOperationException();
    }

    private int getNewId() {
        int id = newId;
        newId = newId + 1;
        return id;
    }

}
