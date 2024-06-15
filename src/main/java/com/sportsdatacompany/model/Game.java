package com.sportsdatacompany.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {

    private int gameId;
    private Team homeTeam;
    private Team awayTeam;
    private int homeTeamScore;
    private int awayTeamScore;

}
