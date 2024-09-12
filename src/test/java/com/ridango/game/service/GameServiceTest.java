package com.ridango.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ridango.game.api.CocktailApiService;
import com.ridango.game.common.HintProvider;
import com.ridango.game.common.InputProvider;
import com.ridango.game.model.Cocktail;
import com.ridango.game.repository.HighscoreRepository;

public class GameServiceTest {

    @Mock
    private CocktailApiService cocktailApiService;

    @Mock
    private HighscoreRepository highscoreRepository;

    @Mock
    private HighscoreService highscoreService;

    @Mock
    private HintProvider hintProvider;

    @Mock
    private InputProvider inputProvider;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsername_EmptyUsername() {
        when(inputProvider.getInput())
            .thenReturn("")  // First call returns empty
            .thenReturn("Valid Username");  // Second call returns valid username

        String username = gameService.getUsername();

        System.out.println("Actual username: '" + username + "'");

        assertEquals("Valid Username", username, "Username should be 'Valid Username'");
    }

    @Test
    public void testGetUsername_ValidUsername() {
        when(inputProvider.getInput()).thenReturn("Valid Username");

        String username = gameService.getUsername();

        System.out.println("Actual username: '" + username + "'");

        assertEquals("Valid Username", username, "Username should be 'Valid Username'");
    }

    @Test
    public void testGetRandomCocktail_NoDuplicateCocktails() {
        Cocktail cocktail1 = createCocktail(1L, "Margarita");
        Cocktail cocktail2 = createCocktail(2L, "Martini");
        Cocktail cocktail3 = createCocktail(3L, "Old Fashioned");
        Cocktail cocktail4 = createCocktail(4L, "Mojito");
        Cocktail cocktail5 = createCocktail(5L, "Negroni");

        when(cocktailApiService.getRandomCocktail())
            .thenReturn(cocktail1)
            .thenReturn(cocktail2)
            .thenReturn(cocktail3)
            .thenReturn(cocktail3)
            .thenReturn(cocktail4)
            .thenReturn(cocktail1)
            .thenReturn(cocktail5);

        when(inputProvider.getInput()).thenReturn("Guess");

        Set<String> cocktailNames = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Cocktail cocktail = gameService.getNewCocktail();
            cocktailNames.add(cocktail.getName());
        }

        assertEquals(5, cocktailNames.size(), "There should be exactly 5 unique cocktails.");
    }

    @Test
    public void testManageGameLoop_NoCocktails() {
        when(cocktailApiService.getRandomCocktail()).thenReturn(null);
        when(inputProvider.getInput()).thenReturn("yes").thenReturn("no");

        int finalScore = gameService.manageGameLoop("Player");

        assertEquals(0, finalScore, "The final score should be 0 when there are no cocktails.");
        verify(cocktailApiService, times(10)).getRandomCocktail();
        verify(inputProvider, times(2)).getInput();
    }

    @Test
    public void testManageGameLoop_UserPlaysOnce() {
        Cocktail mockCocktail1 = createCocktail(1L, "Margarita");

        when(cocktailApiService.getRandomCocktail())
                .thenReturn(mockCocktail1);
        when(inputProvider.getInput())
                .thenReturn("MARGARITA")
                .thenReturn("no");

        String username = "Player";
        int finalScore = gameService.manageGameLoop(username);

        assertEquals(5, finalScore);
        verify(cocktailApiService, times(1)).getRandomCocktail();
        verify(inputProvider, times(2)).getInput();
    }

    @Test
    public void testManageGameLoop_UserFailsSecondTime() {
        Cocktail mockCocktail1 = createCocktail(1L, "Gin tonic");
        Cocktail mockCocktail2 = createCocktail(2L, "Mojito");

        when(cocktailApiService.getRandomCocktail())
                .thenReturn(mockCocktail1)
                .thenReturn(mockCocktail2);
        when(inputProvider.getInput())
                .thenReturn("gin tonic")
                .thenReturn("yes")
                .thenReturn("WrongGuess")  
                .thenReturn("WrongGuess")  
                .thenReturn("WrongGuess")
                .thenReturn("WrongGuess")
                .thenReturn("WrongGuess")
                .thenReturn("no");

        String username = "Player";
        int finalScore = gameService.manageGameLoop(username);

        assertEquals(0, finalScore);
        verify(cocktailApiService, times(2)).getRandomCocktail();
        verify(inputProvider, times(8)).getInput();
    }

    @Test
    public void testManageGameLoop_UserPlaysTwice() {
        Cocktail mockCocktail1 = createCocktail(1L, "Margarita");
        Cocktail mockCocktail2 = createCocktail(2L, "Mojito");

        when(cocktailApiService.getRandomCocktail())
                .thenReturn(mockCocktail1)
                .thenReturn(mockCocktail2);
        when(inputProvider.getInput())
                .thenReturn("Margarita")
                .thenReturn("yes")
                .thenReturn("WrongGuess")  
                .thenReturn("WrongGuess")  
                .thenReturn("Mojito")
                .thenReturn("no");

        String username = "Player";
        int finalScore = gameService.manageGameLoop(username);

        assertEquals(8, finalScore);
        verify(cocktailApiService, times(2)).getRandomCocktail();
        verify(inputProvider, times(6)).getInput();
    }

    private Cocktail createCocktail(Long id, String name) {
        Cocktail cocktail = new Cocktail();
        cocktail.setId(id);
        cocktail.setName(name);
        cocktail.setCategory("Cocktail");
        cocktail.setGlass("Glass");
        cocktail.setInstructions("Instructions");
        cocktail.setImage("image_url");
        cocktail.setIsAlcoholic("Yes");
        return cocktail;
    }
}
