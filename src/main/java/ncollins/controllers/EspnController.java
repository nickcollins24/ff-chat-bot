package ncollins.controllers;

import ncollins.espn.Espn;
import ncollins.espn.EspnDataLoader;
import ncollins.espn.EspnMessageBuilder;
import ncollins.model.espn.Matchup;
import ncollins.model.espn.Score;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("espn")
public class EspnController {
    private Espn espn = new Espn(new EspnDataLoader());
    private EspnMessageBuilder espnMessageBuilder = new EspnMessageBuilder(espn);

    @GetMapping("scores/playoffs")
    public List<Score> getPlayoffScores() {
        return espn.getPlayoffScoresAllTime();
    }

    @GetMapping("matchups/playoffs")
    public List<Matchup> getPlayoffMatchups() {
        return espn.getPlayoffMatchupsAllTime();
    }

    @GetMapping("message/standings/playoffs")
    public String getPlayoffMatchupsMessage(){
        return espnMessageBuilder.buildPlayoffStandingsMessage();
    }
}
