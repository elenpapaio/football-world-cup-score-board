package com.sportradar.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameDto {

    private int gameId;
    private int homeTeamScore;
    private int awayTeamScore;

}
