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
    public String buildScoresMessage(Order order, int total, boolean includePlayoffs){
        List<Score> scores = espn.getScoresSorted(order, total, includePlayoffs);

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(total + " Scores:\\n");

        for(int i=0; i < scores.size(); i++){
            String teamAbbrev = espn.getTeamAbbrev(scores.get(i).getTeamId());
            sb.append(i+1 + ": " + scores.get(i).getPoints() + " - " + teamAbbrev + "(" + scores.get(i).getMatchupPeriodId() + ")\\n");
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
        List<ScheduleItem> matchups = espn.getMatchupsSorted(Order.DESC, count, false);

        StringBuilder sb = new StringBuilder();
            sb.append(" Biggest Blowouts:\\n");
            for(ScheduleItem matchup : matchups){
                ScheduleItem.Residence winner = matchup.getWinner().equals("HOME") ? matchup.getHome() : matchup.getAway();
                ScheduleItem.Residence loser = matchup.getWinner().equals("AWAY") ? matchup.getHome() : matchup.getAway();

                sb.append(String.format("%.2f", Math.abs(matchup.getHome().getTotalPoints() - matchup.getAway().getTotalPoints())) + ": ")
                  .append(espn.getTeamAbbrev(winner.getTeamId()) + " " + winner.getTotalPoints() + " - ")
                  .append(loser.getTotalPoints() + " " + espn.getTeamAbbrev(loser.getTeamId()) + " ")
                  .append("(wk " + matchup.getMatchupPeriodId() + ")\\n");
            }

        return sb.toString();
    }

    /***
     *  Builds message that displays the closest matchups this year.
     */
    public String buildHeartbreaksMessage(int count){
        List<ScheduleItem> matchups = espn.getMatchupsSorted(Order.ASC, count, false);

        StringBuilder sb = new StringBuilder();
        sb.append(" Biggest Heartbreaks:\\n");
        for(ScheduleItem matchup : matchups){
            ScheduleItem.Residence winner = matchup.getWinner().equals("HOME") ? matchup.getHome() : matchup.getAway();
            ScheduleItem.Residence loser = matchup.getWinner().equals("AWAY") ? matchup.getHome() : matchup.getAway();

            sb.append(String.format("%.2f", Math.abs(matchup.getHome().getTotalPoints() - matchup.getAway().getTotalPoints())) + ": ")
                    .append(espn.getTeamAbbrev(loser.getTeamId()) + " " + loser.getTotalPoints() + " - ")
                    .append(winner.getTotalPoints() + " " + espn.getTeamAbbrev(winner.getTeamId()) + " ")
                    .append("(wk " + matchup.getMatchupPeriodId() + ")\\n");
        }

        return sb.toString();
    }
}
