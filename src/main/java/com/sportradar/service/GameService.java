package com.sportradar.service;

import com.sportradar.data.TeamName;
import com.sportradar.model.Game;
import com.sportradar.model.Team;
import com.sportradar.repository.GameRepository;
import com.sportradar.util.InputUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game startGame() {
        String homeTeamName = InputUtils.readStringFromKeyboard();
        String awayTeamName = InputUtils.readStringFromKeyboard();

        if (!TeamName.contains(homeTeamName) || !TeamName.contains(awayTeamName)) {
            throw new RuntimeException(
                    String.format("Invalid one or both team names. Given homeTeamName: %s. Given awayTeamName: %s",
                            homeTeamName, awayTeamName));
        }

        Game startedGame = Game.builder()
                .homeTeam(Team.builder()
                        .name(homeTeamName)
                        .build())
                .awayTeam(Team.builder()
                        .name(awayTeamName)
                        .build())
                .awayTeamScore(0)
                .homeTeamScore(0)
                .build();
        return gameRepository.save(startedGame);
    }

}
