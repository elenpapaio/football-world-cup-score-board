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

        validateStartGameUserInput(homeTeamName, awayTeamName);

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

    public void finishGame() {
        String gameIdInputMessage = "Please provide the id of the game you want to finish: ";
        int gameId = InputUtils.readIntFromKeyboard(gameIdInputMessage);

        Game deletedGame = gameRepository.deleteById(gameId);

        if (deletedGame != null) {
            System.out.println("The game with id " + gameId + " has been removed successfully.");
        } else {
            System.out.println("The game with the given id does not exist.");
        }
    }

    private void validateStartGameUserInput(String homeTeamName, String awayTeamName) {
        if (!TeamName.contains(homeTeamName) || !TeamName.contains(awayTeamName)) {
            throw new RuntimeException(
                    String.format("Invalid one or both team names. Given homeTeamName: %s. Given awayTeamName: %s",
                            homeTeamName, awayTeamName));
        }

        if (homeTeamName.equals(awayTeamName)) {
            throw new RuntimeException("Home team name and away team name cannot be the same.");
        }
    }

}
