package com.ridango.game.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ridango.game.model.Cocktail;

public class CocktailApiServiceTest {

    @Autowired
    private CocktailApiService cocktailApiService = new CocktailApiService();

    @Test
    public void testGetRandomCocktail() {
        Cocktail cocktail = cocktailApiService.getRandomCocktail();
        assertNotNull(cocktail, "Cocktail should not be null");
    }

}
