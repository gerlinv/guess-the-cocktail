package com.ridango.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ridango.game.service.GameService;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	@Autowired
    private GameService gameService;
	
	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Override public void run(String... args) throws Exception {
		gameService.startGame();
	}
}
