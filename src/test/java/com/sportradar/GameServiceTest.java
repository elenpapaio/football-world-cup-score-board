package com.sportradar;

import com.sportradar.model.Game;
import com.sportradar.repository.GameRepository;
import com.sportradar.service.GameService;
import com.sportradar.util.InputUtils;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private InputUtils inputUtils;

    @Mock
    private GameRepository gameRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        gameService = new GameService(gameRepository);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName(value = "It should receive home and away team names from user input and save the started game to the database layer")
    public void startGame() {
        try(MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(InputUtils::readStringFromKeyboard).thenReturn("Uruguay").thenReturn("Italy");

            Game startedGame = gameService.startGame();
            mockInputUtils.verify(InputUtils::readStringFromKeyboard, times(2));
            verify(gameRepository, times(1)).save(startedGame);

            Assertions.assertEquals(startedGame.getHomeTeam().getName(), "Uruguay");
            Assertions.assertEquals(startedGame.getAwayTeam().getName(), "Italy");
        }

    }

}
