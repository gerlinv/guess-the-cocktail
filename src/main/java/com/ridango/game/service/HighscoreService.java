package com.ridango.game.service;

import com.ridango.game.model.Highscore;
import com.ridango.game.repository.HighscoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HighscoreService {

    private Highscore highScore;

    @Autowired
    private HighscoreRepository highscoreRepository;

    public HighscoreService(HighscoreRepository highscoreRepository) {
        this.highscoreRepository = highscoreRepository;
    }

    private void fetchHighScoreFromDatabase() {
        this.highScore = highscoreRepository.findTopByOrderByScoreDesc();
    }

    public void updateHighScore(Integer score, String username) {
        if (highScore == null || score > highScore.getScore()) {
            Highscore newHighscore = new Highscore();
            newHighscore.setScore(score);
            newHighscore.setTimestamp(Timestamp.from(Instant.now()));
            newHighscore.setUsername(username);
            
            highscoreRepository.save(newHighscore);
            highScore = newHighscore;

            System.out.println("CONGRATULATIONS, NEW HIGHSCORE!");
        }
    }

    public void displayHighscore() {
        fetchHighScoreFromDatabase();

        if (highScore != null) {
            LocalDateTime localDateTime = highScore.getTimestamp().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = localDateTime.format(formatter);

            System.out.println("Current highscore: " + highScore.getScore());
            System.out.println("Highscore owner: " + highScore.getUsername());
            System.out.println("Time achieved: " + formattedDateTime);
        } else {
            System.out.println("There is no current highscore!");
        }
    }
    
}
