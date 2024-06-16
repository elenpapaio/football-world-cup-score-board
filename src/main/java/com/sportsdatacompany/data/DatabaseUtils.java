package com.sportsdatacompany.data;

import com.sportsdatacompany.dto.GameDto;

public class DatabaseUtils {

    public static void populateDatabase(InMemoryDatabase inMemoryDatabase) {
        GameDto game1 = GameDto.builder()
                .homeTeamName("Mexico")
                .awayTeamName("Canada")
                .homeTeamScore(0)
                .awayTeamScore(5)
                .build();

        GameDto game2 = GameDto.builder()
                .homeTeamName("Spain")
                .awayTeamName("Brazil")
                .homeTeamScore(10)
                .awayTeamScore(2)
                .build();

        GameDto game3 = GameDto.builder()
                .homeTeamName("Germany")
                .awayTeamName("France")
                .homeTeamScore(2)
                .awayTeamScore(2)
                .build();

        GameDto game4 = GameDto.builder()
                .homeTeamName("Uruguay")
                .awayTeamName("Italy")
                .homeTeamScore(6)
                .awayTeamScore(6)
                .build();

        GameDto game5 = GameDto.builder()
                .homeTeamName("Argentina")
                .awayTeamName("Australia")
                .homeTeamScore(3)
                .awayTeamScore(1)
                .build();

        inMemoryDatabase.insertGame(game1);
        inMemoryDatabase.insertGame(game2);
        inMemoryDatabase.insertGame(game3);
        inMemoryDatabase.insertGame(game4);
        inMemoryDatabase.insertGame(game5);
//        gameList.addAll(new ArrayList<>(Arrays.asList(game1, game2, game3, game4, game5)));
//        inMemoryDatabase.setNewId(6);
    }

}
