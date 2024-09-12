package com.ridango.game.repository;

import com.ridango.game.model.Highscore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighscoreRepository extends JpaRepository<Highscore, Long> {

    Highscore findTopByOrderByScoreDesc();
    
}
