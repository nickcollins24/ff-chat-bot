package ncollins.controllers;

import ncollins.espn.Espn;
import ncollins.espn.EspnDataLoader;
import ncollins.espn.EspnMessageBuilder;
import ncollins.model.espn.Matchup;
import ncollins.model.espn.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("espn")
public class EspnController {
    private Espn espn;

    @Autowired
    public EspnController(Espn espn){
        this.espn = espn;
    }

    @GetMapping("scores/playoffs")
    public List<Score> getPlayoffScores() {
        return espn.getPlayoffScoresAllTime();
    }

    @GetMapping("matchups/playoffs")
    public List<Matchup> getPlayoffMatchups() {
        return espn.getPlayoffMatchupsAllTime();
    }
}
