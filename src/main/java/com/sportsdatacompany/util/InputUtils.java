package com.sportsdatacompany.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtils {

    public static String readStringFromKeyboard(String messageToUser) {
        System.out.println(messageToUser);
        Scanner keyboard = new Scanner(System.in);
        return keyboard.nextLine();
    }

    public static int readIntFromKeyboard(String messageToUser) {
        System.out.println(messageToUser);
        Scanner keyboard = new Scanner(System.in);
        try {
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Invalid input - must be a number");
        }
    }

}
