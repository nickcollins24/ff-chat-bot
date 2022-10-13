package ncollins.controllers;

import ncollins.espn.Espn;
import ncollins.espn.EspnMessageBuilder;
import ncollins.model.Order;
import ncollins.model.espn.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping("playoffs/scores")
    public List<Score> getPlayoffScores() {
        return espn.getPlayoffScoresAllTime();
    }

    @GetMapping("playoffs/matchups")
    public List<Matchup> getPlayoffMatchups() {
        return espn.getPlayoffMatchupsAllTime();
    }

    @GetMapping("matchups/{teamA}/{teamB}")
    public List<Matchup> getMatchupsBetween(@PathVariable String teamA, @PathVariable String teamB) {
        Member memberA = espn.getMemberByTeamAbbrev(teamA);
        Member memberB = espn.getMemberByTeamAbbrev(teamB);
        return espn.getMatchupsBetweenSorted(Order.DESC, memberA, memberB);
    }

    @GetMapping("message/gameday")
    public String getGamedayMessage() {
        return messageBuilder.buildGamedayMessage();
    }

    @GetMapping("message/matchups/{teamA}/{teamB}")
    public String getMatchupsBetweenMessage(@PathVariable String teamA, @PathVariable String teamB){
        return messageBuilder.buildMatchupsMessage(teamA, teamB);
    }

    @GetMapping("trades")
    public List<Trade> getTrades(@RequestParam Integer seasonId, @RequestParam(required = false) Integer lastSeconds){
        if(lastSeconds != null)
            return espn.getTrades(seasonId, System.currentTimeMillis()-(lastSeconds*1000), System.currentTimeMillis());
        else return espn.getTrades(seasonId);
    }

    @GetMapping("transactions")
    public List<Transaction> getTransactions(@RequestParam Integer[] transactionIds, @RequestParam Integer seasonId, @RequestParam Integer lastSeconds){
        return espn.getTransactions(seasonId, System.currentTimeMillis()-(lastSeconds*1000), System.currentTimeMillis(),
                Arrays.asList(transactionIds));
    }
}
