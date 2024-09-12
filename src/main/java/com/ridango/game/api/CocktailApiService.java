package com.ridango.game.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.model.Cocktail;

@Service
public class CocktailApiService {

    private static final String RANDOM_COCKTAIL_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Cocktail getRandomCocktail() {
        try {
            URL url = new URL(RANDOM_COCKTAIL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            return parseCocktailData(inline.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cocktail parseCocktailData(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode drinkNode = rootNode.path("drinks").get(0);

            Long id = drinkNode.path("idDrink").asLong();
            String name = drinkNode.path("strDrink").asText();
            String instructions = drinkNode.path("strInstructions").asText();
            String category = drinkNode.path("strCategory").asText();
            String glass = drinkNode.path("strGlass").asText();
            String image = drinkNode.path("strDrinkThumb").asText();
            String alcoholic = "Alcoholic".equalsIgnoreCase(drinkNode.path("strAlcoholic").asText()) ? "Yes" : "No";

            return new Cocktail(id, name, instructions, category, glass, image, alcoholic);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
