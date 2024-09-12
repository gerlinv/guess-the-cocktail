package com.ridango.game.common;

import org.springframework.stereotype.Component;

import com.ridango.game.model.Cocktail;

@Component
public class HintProvider {
    
    public void provideHint(Cocktail cocktail, int attempts) {
        switch (attempts) {
            case 5:
                System.out.println("Hint 1: Instructions - " + cocktail.getInstructions());
                break;
            case 4:
                System.out.println("Hint 2: Alcoholic - " + cocktail.getIsAlcoholic());
                break;
            case 3:
                System.out.println("Hint 3: Category - " + cocktail.getCategory());
                break;
            case 2:
                System.out.println("Hint 4: Glass - " + cocktail.getGlass());
                break;
            case 1:
                System.out.println("Hint 5: Image - " + cocktail.getImage());
                break;
            default:
                break;
        }
    }

}
