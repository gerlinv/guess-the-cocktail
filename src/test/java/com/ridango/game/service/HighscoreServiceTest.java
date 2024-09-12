package com.ridango.game.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ridango.game.model.Highscore;
import com.ridango.game.repository.HighscoreRepository;

public class HighscoreServiceTest {

    @Mock
    private HighscoreRepository highscoreRepository;

    @InjectMocks
    private HighscoreService highscoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        highscoreService = new HighscoreService(highscoreRepository);
    }

    @Test
    public void testFetchHighscoreFromDatabase_EmptyHighscore() {
        when(highscoreRepository.findTopByOrderByScoreDesc()).thenReturn(null);

        highscoreService.displayHighscore();
        
        verify(highscoreRepository, times(1)).findTopByOrderByScoreDesc();
    }

    @Test
    public void testUpdateHighScore_NewHighScore() {
        Highscore mockHighscore = new Highscore();
        mockHighscore.setScore(50);

        when(highscoreRepository.findTopByOrderByScoreDesc()).thenReturn(mockHighscore);

        highscoreService.updateHighScore(100, "Player1");

        verify(highscoreRepository, times(1)).save(any(Highscore.class));
    }

    @Test
    public void testUpdateHighScore_LowerScoreDoesNotUpdate() {
        Highscore mockHighscore = new Highscore();
        mockHighscore.setScore(150);

        when(highscoreRepository.findTopByOrderByScoreDesc()).thenReturn(mockHighscore);

        highscoreService.updateHighScore(100, "Player1");

        verify(highscoreRepository, times(1)).save(any(Highscore.class)); // No save if the score is lower
    }
}
