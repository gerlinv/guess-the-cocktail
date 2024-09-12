package com.ridango.game.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ridango.game.api.CocktailApiService;
import com.ridango.game.common.CocktailNameUtils;
import com.ridango.game.common.ConsoleUtils;
import com.ridango.game.common.HintProvider;
import com.ridango.game.common.InputProvider;
import com.ridango.game.model.Cocktail;
import com.ridango.game.repository.HighscoreRepository;

@Service
public class GameService {

    private static final int MAX_ATTEMPTS = 5;

    private CocktailApiService cocktailApiService;
    private HighscoreService highscoreService;
    private final Set<Long> usedCocktails = new HashSet<>();
    private final HintProvider hintProvider;
    private final InputProvider inputProvider;

    @Autowired
    public GameService(CocktailApiService cocktailApiService, HighscoreRepository highscoreRepository, HintProvider hintProvider, InputProvider inputProvider) {
        this.cocktailApiService = cocktailApiService;
        this.highscoreService = new HighscoreService(highscoreRepository);
        this.hintProvider = hintProvider;
        this.inputProvider = inputProvider;
    }

    public void startGame() {
        ConsoleUtils.printSectionHeader("WELCOME TO GUESS THE COCKTAIL!");
        highscoreService.displayHighscore();
        ConsoleUtils.printSeparator();
        String username = getUsername();
        
        int score = manageGameLoop(username);

        ConsoleUtils.printSectionHeader("THANK YOU FOR PLAYING!");
        highscoreService.updateHighScore(score, username);

        System.exit(0);
    }

    protected int manageGameLoop(String username) {
        int score = 0;
        boolean playAgain = true;

        while (playAgain) {
            Cocktail cocktail = getNewCocktail();
            if (cocktail == null) {
                System.out.println("Looks like we've run out of cocktails!");
                playAgain = askToPlayNewGame(score, username);
                score = 0;
            } else {
                int roundScore = playRound(cocktail);
                score += roundScore;
    
                if (roundScore == 0) {
                    playAgain = askToPlayNewGame(score, username);
                    score = 0;
                } else {
                    playAgain = askToPlayRoundAgain();
                    if (!playAgain) {
                        System.out.println("Final score: " + score);
                    }
                    // Could also ask to start a new game
                }
            }
        }

        return score;
    }

    protected String getUsername() {
        String username;
        do {
            System.out.print("Enter username > ");
            username = inputProvider.getInput().trim();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            }
        } while (username.isEmpty());
        return username;
    }

    protected int playRound(Cocktail cocktail) {
        String cocktailName = cocktail.getName();
        String maskedName = CocktailNameUtils.maskCocktailName(cocktailName);
        int attempts = MAX_ATTEMPTS;

        while (attempts > 0) {
            displayGuessPrompt(maskedName, attempts);
            displayHint(cocktail, attempts);

            System.out.print("Your guess > ");
            String guess = inputProvider.getInput();
            if (guess.equalsIgnoreCase(cocktailName)) {
                System.out.println("Correct! The cocktail was: " + cocktailName);
                return calculateRoundScore(attempts);
            } else {
                attempts--;
                maskedName = CocktailNameUtils.revealLetters(cocktailName, maskedName, 1);

                if (attempts > 0) {
                    System.out.println("Wrong! Try again.");
                    ConsoleUtils.printLine('*', 20);
                } else {
                    ConsoleUtils.printSectionHeader("GAME OVER!");
                    System.out.println("The cocktail was: " + cocktailName);
                    revealCocktailDetails(cocktail);
                }
            }
        }

        return 0; // No points if all attempts are used
    }

    private void displayGuessPrompt(String maskedName, int attempts) {
        System.out.println("Guess the cocktail: " + maskedName);
        System.out.println("Attempts left: " + attempts);
    }

    private void displayHint(Cocktail cocktail, int attempts) {
        hintProvider.provideHint(cocktail, attempts);
    }

    protected Cocktail getNewCocktail() {
        Cocktail cocktail = null;
        int attempts = 0;
    
        while (attempts < MAX_ATTEMPTS) {
            cocktail = cocktailApiService.getRandomCocktail();
            if (cocktail == null) {
                attempts++;
            } else if (!usedCocktails.contains(cocktail.getId())) {
                usedCocktails.add(cocktail.getId());
                return cocktail;
            } else {
                attempts++;
            }
        }
    
        return null;
    }
    
    private void revealCocktailDetails(Cocktail cocktail) {
        System.out.println("Cocktail Details:");
        System.out.println("Category: " + cocktail.getCategory());
        System.out.println("Glass: " + cocktail.getGlass());
        System.out.println("Instructions: " + cocktail.getInstructions());
        System.out.println("Image: " + cocktail.getImage());
        System.out.println("Alcoholic: " + cocktail.getIsAlcoholic());
    }

    private int calculateRoundScore(int attemptsLeft) {
        return Math.max(0, attemptsLeft);
    }

    protected boolean askToPlayRoundAgain() {
        ConsoleUtils.printSeparator();
        System.out.print("Do you want to go to the next round? (yes/no) > ");
        return inputProvider.getInput().equalsIgnoreCase("yes");
    }

    private boolean askToPlayNewGame(int score, String username) {
        ConsoleUtils.printSeparator();
        System.out.println("Final score: " + score);
        highscoreService.updateHighScore(score, username);
        System.out.print("Do you want to start a new game? (yes/no) > ");
        usedCocktails.clear();
        return inputProvider.getInput().equalsIgnoreCase("yes");
    }

}
