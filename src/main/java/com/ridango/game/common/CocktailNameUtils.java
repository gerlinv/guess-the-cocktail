package com.ridango.game.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CocktailNameUtils {

    public static String maskCocktailName(String cocktailName) {
        return cocktailName.replaceAll("[^ ]", "_");
    }

    public static String revealLetters(String cocktailName, String maskedName, int attempts) {
        StringBuilder sb = new StringBuilder(maskedName);

        int revealCount = calculateRevealCount(cocktailName, sb, attempts);

        for (int i = 0; i < revealCount; i++) {
            int index = getRandomIndex(cocktailName, sb.toString());
            if (index >= 0) {
                sb.setCharAt(index, cocktailName.charAt(index));
            }
        }

        return sb.toString();
    }

    private static int calculateRevealCount(String cocktailName, StringBuilder sb, int attempts) {
        List<Integer> unrevealedIndices = getUnrevealedIndices(cocktailName, sb.toString());
        int unrevealedCount = unrevealedIndices.size();
        int nameLength = cocktailName.replace(" ", "").length();
        if (unrevealedCount > 5) {
            // If there are more than 5 letters left unrevealed, apply a more lenient approach
            return Math.min(attempts * 2, nameLength / 2 + 1);
        } else {
            // For shorter names or fewer unrevealed letters, reveal a maximum of 1 letter per attempt
            return 1;
        }
    }

    private static List<Integer> getUnrevealedIndices(String cocktailName, String maskedName) {
        List<Integer> unrevealedIndices = new ArrayList<>();
        for (int i = 0; i < cocktailName.length(); i++) {
            if (maskedName.charAt(i) == '_' && cocktailName.charAt(i) != ' ') {
                unrevealedIndices.add(i);
            }
        }
        return unrevealedIndices;
    }

    private static int getRandomIndex(String cocktailName, String maskedName) {
        List<Integer> unrevealedIndices = new ArrayList<>();
        for (int i = 0; i < cocktailName.length(); i++) {
            if (maskedName.charAt(i) == '_' && cocktailName.charAt(i) != ' ') {
                unrevealedIndices.add(i);
            }
        }

        if (unrevealedIndices.isEmpty()) {
            return -1;
        }

        Random random = new Random();
        return unrevealedIndices.get(random.nextInt(unrevealedIndices.size()));
    }

}
