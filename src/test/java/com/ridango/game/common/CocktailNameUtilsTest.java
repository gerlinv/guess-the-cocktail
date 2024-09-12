package com.ridango.game.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CocktailNameUtilsTest {

    @Test
    public void testMaskCocktailName_withSpaces() {
        String cocktailName = "Gin Tonic";
        String expectedMaskedName = "___ _____";
        String actualMaskedName = CocktailNameUtils.maskCocktailName(cocktailName);

        assertEquals(expectedMaskedName, actualMaskedName, "The masked name should correctly replace letters with underscores, leaving spaces untouched.");
    }

    @Test
    public void testMaskCocktailName_noSpaces() {
        String cocktailName = "Margarita";
        String expectedMaskedName = "_________";
        String actualMaskedName = CocktailNameUtils.maskCocktailName(cocktailName);

        assertEquals(expectedMaskedName, actualMaskedName, "The masked name should replace all letters with underscores.");
    }

    @Test
    public void testRevealLetters_revealsOneLetter() {
        String cocktailName = "Gin";
        String maskedName = "___";
        String revealedName = CocktailNameUtils.revealLetters(cocktailName, maskedName, 5);

        assertNotEquals(maskedName, revealedName, "The masked name should reveal at least one letter.");
        assertTrue(revealedName.contains("G") || revealedName.contains("i") || revealedName.contains("n"), 
            "The revealed name should contain a letter from the original cocktail name.");
    }

    @Test
    public void testGetRandomIndex_allRevealed() {
        String cocktailName = "Gin";
        String maskedName = "Gin";

        int index = CocktailNameUtils.revealLetters(cocktailName, maskedName, 1).indexOf("_");
        assertEquals(-1, index, "The method should return -1 when all letters are already revealed.");
    }

}
