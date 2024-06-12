package com.sportradar;

import com.sportradar.model.Game;
import com.sportradar.service.GameService;
import com.sportradar.util.InputUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService = new GameService();

    @Mock
    private InputUtils inputUtils;

    @Test
    @DisplayName(value = "It should receive home and away team names from user input")
    public void startGame() {
        try(MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(InputUtils::readStringFromKeyboard).thenReturn("Uruguay").thenReturn("Italy");

            Game startedGame = gameService.startGame();
            mockInputUtils.verify(InputUtils::readStringFromKeyboard, times(2));
            Assertions.assertEquals(startedGame.getHomeTeam().getName(), "Uruguay");
            Assertions.assertEquals(startedGame.getAwayTeam().getName(), "Italy");
        }

    }

}
