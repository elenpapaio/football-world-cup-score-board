package com.sportsdatacompany.util;

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
        return keyboard.nextInt();
    }

}
