package ncollins.controllers;

import ncollins.espn.Espn;
import ncollins.espn.EspnMessageBuilder;
import ncollins.model.Order;
import ncollins.model.espn.Matchup;
import ncollins.model.espn.Member;
import ncollins.model.espn.Player;
import ncollins.model.espn.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("espn")
public class EspnController {
    private Espn espn;
    private EspnMessageBuilder messageBuilder;

    @Autowired
    public EspnController(Espn espn, EspnMessageBuilder messageBuilder){
        this.espn = espn;
        this.messageBuilder = messageBuilder;
    }

    @GetMapping("players/{playerId}")
    public Player getPlayer(@PathVariable Integer playerId){ return espn.getPlayer(playerId); }

    @GetMapping("scores/playoffs")
    public List<Score> getPlayoffScores() {
        return espn.getPlayoffScoresAllTime();
    }

    @GetMapping("matchups/playoffs")
    public List<Matchup> getPlayoffMatchups() {
        return espn.getPlayoffMatchupsAllTime();
    }

    @GetMapping("matchups/{teamA}/{teamB}")
    public List<Matchup> getMatchupsBetween(@PathVariable String teamA, @PathVariable String teamB) {
        Member memberA = espn.getMemberByTeamAbbrev(teamA);
        Member memberB = espn.getMemberByTeamAbbrev(teamB);
        return espn.getMatchupsBetweenSorted(Order.DESC, memberA, memberB);
    }

    @GetMapping("gameday")
    public String getGamedayMessage() {
        return messageBuilder.buildGamedayMessage();
    }
}
