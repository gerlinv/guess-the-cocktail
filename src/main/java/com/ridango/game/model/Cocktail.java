package com.ridango.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cocktail {
    
    private Long id;
    @NonNull private String name;
    @NonNull private String instructions;
    @NonNull private String category;
    @NonNull private String glass;
    @NonNull private String image;
    // Could be a boolean
    @NonNull private String isAlcoholic;
    
}
