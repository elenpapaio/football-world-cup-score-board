package com.sportradar.data;

import com.sportradar.model.Game;
import com.sportradar.model.Team;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {
    @Getter
    private final List<Game> games = new ArrayList<>();
    private int newId = 1;

    public Game insertGame(Game game) {
        Game objectToInsert = Game.builder()
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
        games.add(objectToInsert);
        return objectToInsert;
    }

    private int getNewId() {
        int id = newId;
        newId = newId + 1;
        return id;
    }

}
