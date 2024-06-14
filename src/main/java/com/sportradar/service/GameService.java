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
        String homeTeamInputMessage = "Please enter home team name: ";
        String homeTeamName = InputUtils.readStringFromKeyboard(homeTeamInputMessage);

        String awayTeamInputMessage = "Please enter away team name: ";
        String awayTeamName = InputUtils.readStringFromKeyboard(awayTeamInputMessage);

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
        Game savedGame = gameRepository.save(startedGame);
        System.out.println("Started game with id: " + savedGame.getGameId());
        return savedGame;
    }

}
