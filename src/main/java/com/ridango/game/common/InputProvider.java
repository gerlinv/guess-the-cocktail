package com.ridango.game.common;

import java.util.Scanner;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;

@Component
public class InputProvider {

    private final Scanner scanner;

    public InputProvider() {
        this.scanner = new Scanner(System.in);
    }

    public String getInput() {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                return "no";
            }
        // Could ask again for user input, but for the sake of simplicity, left it like this
        } catch (NoResultException e) {
            return "no";
        }
    }
}

