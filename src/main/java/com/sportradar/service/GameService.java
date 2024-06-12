package com.sportradar.service;

import com.sportradar.model.Game;
import com.sportradar.model.Team;
import com.sportradar.util.InputUtils;

public class GameService {

    public Game startGame() {
        String homeTeamName = InputUtils.readStringFromKeyboard();
        String awayTeamName = InputUtils.readStringFromKeyboard();
        return Game.builder()
                .homeTeam(Team.builder()
                        .name(homeTeamName)
                        .build())
                .awayTeam(Team.builder()
                        .name(awayTeamName)
                        .build())
                .awayTeamScore(0)
                .homeTeamScore(0)
                .build();
    }

}
