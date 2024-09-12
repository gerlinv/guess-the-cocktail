package com.ridango.game.common;

public class ConsoleUtils {

    public static void printSeparator() {
        System.out.println("========================================");
    }

    public static void printSectionHeader(String title) {
        System.out.println("======= " + title + " =======");
    }

    public static void printLine(char character, int length) {
        System.out.println(String.valueOf(character).repeat(length));
    }

}
