package com.sportsdatacompany;

import com.sportsdatacompany.dto.GameDto;
import com.sportsdatacompany.model.Game;
import com.sportsdatacompany.model.Team;
import com.sportsdatacompany.repository.GameRepository;
import com.sportsdatacompany.service.GameService;
import com.sportsdatacompany.util.InputUtils;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;

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
            when(gameRepository.save(any())).thenReturn(Game.builder()
                    .gameId(1)
                    .homeTeam(Team.builder().name("Uruguay").build())
                    .awayTeam(Team.builder().name("Italy").build())
                    .homeTeamScore(0)
                    .awayTeamScore(0)
                    .build());

            Game startedGame = gameService.startGame();

            mockInputUtils.verify(() -> InputUtils.readStringFromKeyboard(anyString()), times(2));
            verify(gameRepository, times(1)).save(any(GameDto.class));
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
                    .homeTeam(Team.builder().name("Spain").build())
                    .awayTeam(Team.builder().name("Brazil").build())
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

    @Test
    @DisplayName("updateScore - It should receive the game id and home and away team scores to be updated from " +
            "user input and print success message after successful update to the db layer")
    public void updateScore_test1() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readIntFromKeyboard(anyString()))
                    .thenReturn(1) //game id
                    .thenReturn(1) //home team score
                    .thenReturn(0); //away team score
            when(gameRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Game.builder()
                    .gameId(1)
                    .homeTeam(Team.builder().name("Spain").build())
                    .awayTeam(Team.builder().name("Brazil").build())
                    .homeTeamScore(0)
                    .awayTeamScore(0)
                    .build()));

            when(gameRepository.update(any())).thenReturn(Game.builder()
                    .gameId(1)
                    .homeTeam(Team.builder().name("Spain").build())
                    .awayTeam(Team.builder().name("Brazil").build())
                    .homeTeamScore(1)
                    .awayTeamScore(0)
                    .build());


            gameService.updateScore();

            mockInputUtils.verify(() -> InputUtils.readIntFromKeyboard(anyString()), times(3));
            verify(gameRepository, times(1)).findById(1);
            verify(gameRepository, times(1)).update(any());
            assertEquals("The score for the game Spain - Brazil has been updated successfully. New score: 1 - 0.\r\n", outContent.toString());
        }
    }

    @Test
    @DisplayName("updateScore - It should throw exception when receives score from user input smaller than the existing one")
    public void updateScore_test2() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readIntFromKeyboard(anyString()))
                    .thenReturn(1) //game id
                    .thenReturn(1) //home team score
                    .thenReturn(0); //away team score
            when(gameRepository.findById(anyInt())).thenReturn(Optional.ofNullable(Game.builder()
                    .gameId(1)
                    .homeTeam(Team.builder().name("Spain").build())
                    .awayTeam(Team.builder().name("Brazil").build())
                    .homeTeamScore(2)
                    .awayTeamScore(0)
                    .build()));

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> gameService.updateScore());
            assertEquals("The given score for home/away team cannot be smaller than the already existing one.",
                    exception.getMessage());
            mockInputUtils.verify(() -> InputUtils.readIntFromKeyboard(anyString()), times(3));
            verify(gameRepository, times(1)).findById(1);
            verify(gameRepository, times(0)).update(any());
        }
    }

    @Test
    @DisplayName("updateScore - It should throw exception when the game id from user input does not exist in the database layer")
    public void updateScore_test3() {
        try (MockedStatic<InputUtils> mockInputUtils = Mockito.mockStatic(InputUtils.class)) {
            mockInputUtils.when(() -> InputUtils.readIntFromKeyboard(anyString()))
                    .thenReturn(1) //game id
                    .thenReturn(1) //home team score
                    .thenReturn(0); //away team score
            when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> gameService.updateScore());
            assertEquals("Game with id 1 not found.", exception.getMessage());
            mockInputUtils.verify(() -> InputUtils.readIntFromKeyboard(anyString()), times(3));
            verify(gameRepository, times(1)).findById(1);
            verify(gameRepository, times(0)).update(any());
        }
    }

    @Test
    @DisplayName("getGamesByTotalScore - It should return the given list sorted by total score")
    public void getGamesByTotalScore() {
        Game game1 = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder().name("Italy").build())
                .awayTeam(Team.builder().name("Uruguay").build())
                .homeTeamScore(1)
                .awayTeamScore(1)
                .build();

        Game game2 = Game.builder()
                .gameId(2)
                .homeTeam(Team.builder().name("Spain").build())
                .awayTeam(Team.builder().name("Brazil").build())
                .homeTeamScore(2)
                .awayTeamScore(1)
                .build();

        Game game3 = Game.builder()
                .gameId(2)
                .homeTeam(Team.builder().name("Spain").build())
                .awayTeam(Team.builder().name("Italy").build())
                .homeTeamScore(1)
                .awayTeamScore(1)
                .build();

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2, game3));

        gameService.getGamesByTotalScore();

        assertEquals("Spain 2 - Brazil 1\r\nSpain 1 - Italy 1\r\nItaly 1 - Uruguay 1\r\n", outContent.toString());
    }

    @Test
    @DisplayName("printExistingGames - It should print the existing in the database list")
    public void printExistingGames() {
        Game game1 = Game.builder()
                .gameId(1)
                .homeTeam(Team.builder().name("Italy").build())
                .awayTeam(Team.builder().name("Uruguay").build())
                .homeTeamScore(1)
                .awayTeamScore(1)
                .build();

        Game game2 = Game.builder()
                .gameId(2)
                .homeTeam(Team.builder().name("Spain").build())
                .awayTeam(Team.builder().name("Brazil").build())
                .homeTeamScore(2)
                .awayTeamScore(1)
                .build();

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        gameService.printExistingGames();

        assertEquals("Scoreboard:\r\nItaly - Uruguay: 1 - 1, id: 1\r\nSpain - Brazil: 2 - 1, id: 2\r\n\r\n",
                outContent.toString());
    }

}
