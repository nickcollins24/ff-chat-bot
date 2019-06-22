package ncollins.espn;

import ncollins.model.Favor;
import ncollins.model.Order;
import ncollins.model.espn.Outcome;
import ncollins.model.espn.Position;
import ncollins.model.espn.Team;

import java.time.Year;

public class EspnMessageBuilder {
    Espn espn = new Espn();

    /***
     *  scores {top|bottom} [TOTAL] -- top/bottom scores this year.
     */
    public String buildScoresMessage(Order order, int total){
        return "TODO: buildScoresMessage";
    }

    /***
     *  matchups [WEEK] -- matchups for given week. if week==null, show this week
     *  matchups [TEAM0] [TEAM1] -- all time matchup stats
     */
    public String buildMatchupsMessage(int week){
        return "TODO: buildMatchupsMessage";
    }
    public String buildMatchupsMessage(Team t0, Team t1){
        return "TODO: buildMatchupsMessage";
    }

    /***
     *  standings -- standings this year.
     */
    public String buildStandingsMessage(){
        return "TODO: buildStandingsMessage";
    }

    /***
     *  points [for|against] through [WEEK] {top|bottom} [TOTAL] -- most/least points for or against through given week
     */
    public String buildPointsMessage(Favor favor, int week, Order order, int total){
        return "TODO: buildPointsMessage";
    }

    /***
     *  players {top|bottom} [TOTAL] [POSITION] -- years top/worst players. if year==null, show this week
     */
    public String buildPlayersMessage(Order order, int total, Position position){
        return "TODO: buildPlayersMessage";
    }

    /***
     *  streaks [POINT_TOTAL] points [TOTAL] -- longest streaks of point_total >= points
     */
    public String buildPointsStreakMessage(int points, int total){
        return "TODO: buildPointsStreakMessage";
    }

    /***
     *  streaks {win|loss} [TOTAL] -- longest regular season win/loss streaks
     */
    public String buildOutcomeStreakMessage(Outcome outcome, int total){
        return "TODO: buildOutcomeStreakMessage";
    }

    /***
     *  jujus -- this years jujus
     */
    public String buildJujusMessage(){
        return "TODO: jujus";
    }

    /***
     *  salties -- this years salties
     */
    public String buildSaltiesMessage(){
        return "TODO: salties";
    }

    /***
     *  blowouts [TOTAL] -- biggest blowouts ever
     */
    public String buildBlowoutsMessage(int count){
        return "TODO: blowouts " + count;
    }

    /***
     *  heartbreaks [TOTAL] -- closest games ever
     */
    public String buildHeartbreaksMessage(int count){
        return "TODO: heartbreaks " + count;
    }
}
