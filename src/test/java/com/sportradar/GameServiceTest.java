package com.sportradar;

import com.sportradar.model.Game;
import com.sportradar.model.Team;
import com.sportradar.repository.GameRepository;
import com.sportradar.service.GameService;
import com.sportradar.util.InputUtils;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private InputUtils inputUtils;

    @Mock
    private GameRepository gameRepository;

    private AutoCloseable closeable;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        gameService = new GameService(gameRepository);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        System.setOut(originalOut);
        closeable.close();
    }

    @Test
    @DisplayName(value = "startGame - It should receive home and away team names from user input and save the started game to the database layer")
    public void startGame_test1() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readStringFromKeyboard(anyString()))
                    .thenReturn("Uruguay")
                    .thenReturn("Italy");
            when(gameRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

            Game startedGame = gameService.startGame();

            mockInputUtils.verify(() -> InputUtils.readStringFromKeyboard(anyString()), times(2));
            verify(gameRepository, times(1)).save(startedGame);
            assertEquals(startedGame.getHomeTeam().getName(), "Uruguay");
            assertEquals(startedGame.getAwayTeam().getName(), "Italy");
        }
    }

    @Test
    @DisplayName(value = "startGame - It should throw exception when receives invalid team name from user input")
    public void startGame_test2() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readStringFromKeyboard(anyString()))
                    .thenReturn("abc")
                    .thenReturn("Italy");

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> gameService.startGame());
            assertEquals("Invalid one or both team names. Given homeTeamName: abc. Given awayTeamName: Italy",
                    exception.getMessage());
            mockInputUtils.verify(() -> InputUtils.readStringFromKeyboard(anyString()), times(2));
            verify(gameRepository, times(0)).save(any());
        }
    }

    @Test
    @DisplayName(value = "startGame - It should throw exception when receives the same name for both teams from user input")
    public void startGame_test3() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readStringFromKeyboard(anyString()))
                    .thenReturn("Italy")
                    .thenReturn("Italy");

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> gameService.startGame());
            assertEquals("Home team name and away team name cannot be the same.",
                    exception.getMessage());
            mockInputUtils.verify(() -> InputUtils.readStringFromKeyboard(anyString()), times(2));
            verify(gameRepository, times(0)).save(any());
        }
    }

    @Test
    @DisplayName(value = "finishGame - It should receive the game id to be finished from user input and print success " +
            "message after successful removal from the db layer")
    public void finishGame_test1() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readIntFromKeyboard(anyString()))
                    .thenReturn(1);
            when(gameRepository.deleteById(anyInt())).thenReturn(Game.builder()
                    .gameId(1)
                    .homeTeam(Team.builder()
                            .name("Spain")
                            .build())
                    .awayTeam(Team.builder()
                            .name("Brazil")
                            .build())
                    .awayTeamScore(0)
                    .homeTeamScore(0)
                    .build());

            gameService.finishGame();

            mockInputUtils.verify(() -> InputUtils.readIntFromKeyboard(anyString()), times(1));
            verify(gameRepository, times(1)).deleteById(1);
            assertEquals("The game with id 1 has been removed successfully.\r\n", outContent.toString());
        }
    }

    @Test
    @DisplayName(value = "finishGame - It should receive the game id to be finished from user input and print " +
            "appropriate message after failed removal from the db layer")
    public void finishGame_test2() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readIntFromKeyboard(anyString()))
                    .thenReturn(5);
            when(gameRepository.deleteById(anyInt())).thenReturn(null);

            gameService.finishGame();

            mockInputUtils.verify(() -> InputUtils.readIntFromKeyboard(anyString()), times(1));
            verify(gameRepository, times(1)).deleteById(5);
            assertEquals("The game with the given id does not exist.\r\n", outContent.toString());
        }
    }

}
