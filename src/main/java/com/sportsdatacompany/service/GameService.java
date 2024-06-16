package com.sportsdatacompany.service;

import com.sportsdatacompany.data.TeamName;
import com.sportsdatacompany.dto.GameDto;
import com.sportsdatacompany.model.Game;
import com.sportsdatacompany.repository.GameRepository;
import com.sportsdatacompany.util.InputUtils;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game startGame() {
        String homeTeamInputMessage = "Please enter home team name: ";
        String homeTeamName = InputUtils.readStringFromKeyboard(homeTeamInputMessage);

        String awayTeamInputMessage = "Please enter away team name: ";
        String awayTeamName = InputUtils.readStringFromKeyboard(awayTeamInputMessage);

        validateStartGameUserInput(homeTeamName, awayTeamName);

        GameDto startedGameDto = GameDto.builder()
                .homeTeamScore(0)
                .awayTeamScore(0)
                .homeTeamName(homeTeamName)
                .awayTeamName(awayTeamName)
                .build();

        Game savedGame = gameRepository.save(startedGameDto);
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

    public void updateScore() {
        String gameIdInputMessage = "Please provide the id of the game you want to update its score: ";
        int gameId = InputUtils.readIntFromKeyboard(gameIdInputMessage);

        String homeTeamScoreInputMessage = "Please provide the score of the home team: ";
        int homeTeamScore = InputUtils.readIntFromKeyboard(homeTeamScoreInputMessage);

        String awayTeamScoreInputMessage = "Please provide the score of the away team: ";
        int awayTeamScore = InputUtils.readIntFromKeyboard(awayTeamScoreInputMessage);

        Game gameToBeUpdated = gameRepository.findById(gameId).orElse(null);
        validateUpdateScoreUserInput(gameToBeUpdated, gameId, homeTeamScore, awayTeamScore);

        GameDto gameDto = GameDto.builder()
                .gameId(gameId)
                .homeTeamScore(homeTeamScore)
                .awayTeamScore(awayTeamScore)
                .build();

        Game updatedGame = gameRepository.update(gameDto);

        System.out.printf("The score for the game %s - %s has been updated successfully. New score: %s - %s.\r\n",
                updatedGame.getHomeTeam().getName(), updatedGame.getAwayTeam().getName(),
                updatedGame.getHomeTeamScore(), updatedGame.getAwayTeamScore());

    }

    public void getGamesByTotalScore() {
        List<Game> existingGames = gameRepository.findAll();
        Collections.reverse(existingGames);

        List<Game> gamesSortedByTotalScore = existingGames.stream()
                .sorted((g1, g2) -> {
                    int sumOfScoresG1 = g1.getAwayTeamScore() + g1.getHomeTeamScore();
                    int sumOfScoresG2 = g2.getAwayTeamScore() + g2.getHomeTeamScore();
                    if (sumOfScoresG1 == sumOfScoresG2)
                        return 0;
                    return sumOfScoresG1 < sumOfScoresG2 ? 1 : -1;
                }).
                collect(Collectors.toList());

        for (Game game : gamesSortedByTotalScore) {
            System.out.printf("%s %s - %s %s\r\n", game.getHomeTeam().getName(), game.getHomeTeamScore(),
                    game.getAwayTeam().getName(), game.getAwayTeamScore());
        }

    }

    public void printExistingGames() {
        List<Game> existingGames = gameRepository.findAll();
        System.out.println("Scoreboard:");
        for (Game game : existingGames) {
            System.out.printf("%s - %s: %s - %s, id: %s\r\n", game.getHomeTeam().getName(), game.getAwayTeam().getName(),
                    game.getHomeTeamScore(), game.getAwayTeamScore(), game.getGameId());
        }
        System.out.println();
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

    private void validateUpdateScoreUserInput(Game gameToBeUpdated, int gameId, int homeTeamScore, int awayTeamScore) {
        if (gameToBeUpdated == null) {
            throw new RuntimeException("Game with id " + gameId + " not found.");
        }
        if (homeTeamScore < gameToBeUpdated.getHomeTeamScore() || awayTeamScore < gameToBeUpdated.getAwayTeamScore()) {
            throw new RuntimeException(
                    "The given score for home/away team cannot be smaller than the already existing one.");
        }
    }

}
