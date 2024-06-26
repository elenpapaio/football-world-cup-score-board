package com.sportsdatacompany;

import com.sportsdatacompany.util.InputUtils;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputUtilsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    @DisplayName("readStringFromKeyboard - It should read a String value from keyboard")
    public void readStringFromKeyboard() {
        provideInput("Italy");
        String input = InputUtils.readStringFromKeyboard("insert team name: ");

        assertEquals("Italy", input);
        assertEquals("insert team name: \r\n", outContent.toString());
    }

    @Test
    @DisplayName("readIntFromKeyboard - It should read an int value from keyboard")
    public void readIntFromKeyboard_test1() {
        provideInput("1");
        int input = InputUtils.readIntFromKeyboard("insert the id of the game you want to finish: ");

        assertEquals(1, input);
        assertEquals("insert the id of the game you want to finish: \r\n", outContent.toString());
    }

    @Test
    @DisplayName("readIntFromKeyboard - It should throw exception when reading an invalid value from keyboard")
    public void readIntFromKeyboard_test2() {
        provideInput("test");

        InputMismatchException exception = Assertions.assertThrows(InputMismatchException.class,
                () -> InputUtils.readIntFromKeyboard("insert the id of the game you want to finish: "));
        assertEquals("Invalid input - must be a number",
                exception.getMessage());
    }

}
