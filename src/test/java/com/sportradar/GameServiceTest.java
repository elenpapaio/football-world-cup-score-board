package com.sportradar;

import com.sportradar.model.Game;
import com.sportradar.repository.GameRepository;
import com.sportradar.service.GameService;
import com.sportradar.util.InputUtils;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    @DisplayName(value = "startGame - It should receive home and away team names from user input and save the started game to the database layer")
    public void startGame_test1() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(InputUtils::readStringFromKeyboard)
                    .thenReturn("Uruguay")
                    .thenReturn("Italy");
            when(gameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

            Game startedGame = gameService.startGame();
            mockInputUtils.verify(InputUtils::readStringFromKeyboard, times(2));
            verify(gameRepository, times(1)).save(startedGame);

            assertEquals(startedGame.getHomeTeam().getName(), "Uruguay");
            assertEquals(startedGame.getAwayTeam().getName(), "Italy");
        }
    }

    @Test
    @DisplayName(value = "startGame - It should throw exception when receives invalid team name from user input")
    public void startGame_test2() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(InputUtils::readStringFromKeyboard)
                    .thenReturn("abc")
                    .thenReturn("Italy");

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> gameService.startGame());
            assertEquals("Invalid one or both team names. Given homeTeamName: abc. Given awayTeamName: Italy",
                    exception.getMessage());

            mockInputUtils.verify(InputUtils::readStringFromKeyboard, times(2));
            verify(gameRepository, times(0)).save(any());
        }
    }

}
