package ncollins.espn;

import ncollins.model.Order;
import ncollins.model.espn.*;
import java.util.List;

public class EspnMessageBuilder {
    Espn espn = new Espn();
    private static final String NOPE = "i can't do that yet.";

    /***
     *  Builds message that displays the best/worst scores by a team this year
     */
    public String buildScoresMessage(Order order, int total){
        List<Score> scores = espn.getScores(order, total);

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(total + " Scores:\\n");
        for(Score score : scores){
            String teamAbbrev = espn.getTeamAbbrev(score.getTeamId());
            sb.append(score.getPoints() + " - " + teamAbbrev + "(" + score.getMatchupPeriodId() + ")\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the most/least points by a fantasy player in a week
     */
    public String buildPlayersMessage(Order order, int total, Position position){
        return NOPE;
    }

    /***
     *  Builds message that displays the longest winning/losing streaks this year
     */
    public String buildOutcomeStreakMessage(Outcome outcome, int total){
        return NOPE;
    }

    /***
     *  Builds message that displays the matchups for the current week
     */
    public String buildMatchupsMessage(){
        return NOPE;
    }

    /***
     *  Builds message that displays current standings this year
     */
    public String buildStandingsMessage(){
        List<Team> teams = espn.getTeams();

        StringBuilder sb = new StringBuilder();
        sb.append("Standings:\\n");
        for(Team team : teams){
            sb.append(team.getAbbrev() + " ")
              .append(team.getRecord().getOverall().getWins() + "-" + team.getRecord().getOverall().getLosses() + " ")
              .append(String.format("%.3f", team.getRecord().getOverall().getPercentage()) + " ")
              .append(String.format("%.3f", team.getRecord().getOverall().getPointsFor()) + " ")
              .append(String.format("%.3f", team.getRecord().getOverall().getPointsAgainst()) + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays all jujus this year.
     *  Definition of a Juju: TODO
     */
    public String buildJujusMessage(){
        return NOPE;
    }

    /***
     *  Builds message that displays all salties this year.
     *  Definition of a Salty: TODO
     */
    public String buildSaltiesMessage(){
        return NOPE;
    }

    /***
     *  Builds message that displays the biggest blowout matchups this year.
     */
    public String buildBlowoutsMessage(int count){
        return NOPE;
    }

    /***
     *  Builds message that displays the closest matchups this year.
     */
    public String buildHeartbreaksMessage(int count){
        return NOPE;
    }
}
