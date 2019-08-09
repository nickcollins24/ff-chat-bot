package ncollins.espn;

import ncollins.model.Order;
import ncollins.model.espn.*;
import java.util.List;

public class EspnMessageBuilder {
    private Espn espn = new Espn();
    private static final String NOPE = "i can't do that yet.";

    /***
     *  Builds message that displays the best/worst scores by a team all-time
     */
    public String buildScoresMessage(Order order, int total, boolean includePlayoffs){
        return buildScoresMessage(order, total, null, includePlayoffs);
    }

    /***
     *  Builds message that displays the best/worst scores by a team this year
     */
    public String buildScoresMessageCurrentYear(Order order, int total, boolean includePlayoffs){
        return buildScoresMessage(order, total, espn.getCurrentSeasonId(), includePlayoffs);
    }

    /***
     *  Builds message that displays the best/worst scores by a team
     */
    public String buildScoresMessage(Order order, int total, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = espn.getScoresSorted(order, total, seasonId, includePlayoffs);
        String timePeriod = seasonId == null ? " All Time" : " " + String.valueOf(seasonId);

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(total + " Scores" + timePeriod + ":\\n");

        for(int i=0; i < scores.size(); i++){
            String optionalYear = seasonId == null ? "/" + scores.get(i).getSeasonId() : "";
            Member member = espn.getMemberByTeamId(scores.get(i).getTeamId(), scores.get(i).getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();
            sb.append(i+1 + ": " + scores.get(i).getPoints() + " - " + memberName + " (" + scores.get(i).getMatchupPeriodId() + optionalYear + ")\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the most/least points by a fantasy player in a week all-time
     */
    public String buildPlayersMessage(Order order, int total, Position position){
        return buildPlayersMessage(order, total, null, position);
    }

    /***
     *  Builds message that displays the most/least points by a fantasy player in a week
     *  TODO: implement buildPlayersMessage
     */
    public String buildPlayersMessage(Order order, int total, Integer week, Position position){
        return NOPE;
    }

    /***
     *  Builds message that displays the most/least points by a fantasy player in a week
     */
    public String buildPlayersMessageCurrentWeek(Order order, int total, Position position){
        return buildPlayersMessage(order, total, null, position);
    }

    /***
     *  Builds message that displays the longest winning/losing streaks all-time
     */
    public String buildOutcomeStreakMessage(Outcome outcome, int total){
        return buildOutcomeStreakMessage(outcome, null, total);
    }

    /***
     *  Builds message that displays the longest winning/losing streaks
     *  TODO: implement buildOutcomeStreakMessage
     */
    public String buildOutcomeStreakMessage(Outcome outcome, Integer seasonId, int total){
        return NOPE;
    }

    /***
     *  Builds message that displays matchup history between two teams
     *  TODO: implement buildMatchupsMessage
     */
    public String buildMatchupsMessage(String team0, String team1){
        return NOPE;
    }

    /***
     *  Builds message that displays current standings
     */
    public String buildStandingsMessage(){
        List<Team> teams = espn.getTeamsSorted(Order.DESC, null, espn.getCurrentSeasonId());

        StringBuilder sb = new StringBuilder();
        sb.append("Standings:\\n");
        for(Team team : teams){
            sb.append(espn.getTeamAbbrev(team.getId(), team.getSeasonId()) + " ")
                    .append(team.getRecord().getOverall().getWins() + "-" + team.getRecord().getOverall().getLosses() + " ")
                    .append(String.format("%.1f", team.getRecord().getOverall().getPercentage()) + " ")
                    .append(String.format("%.1f", team.getRecord().getOverall().getPointsFor()) + " ")
                    .append(String.format("%.1f", team.getRecord().getOverall().getPointsAgainst()) + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the best/worst records by a team this year
     */
    public String buildRecordsMessageCurrentYear(Order order, int total){
        return buildRecordsMessage(order, total, espn.getCurrentSeasonId());
    }

    /***
     *  Builds message that displays best/worst records all-time
     */
    public String buildRecordsMessage(Order order, int total){
        return buildRecordsMessage(order, total, null);
    }

    /***
     *  Builds message that displays best/worst records in given year
     */
    public String buildRecordsMessage(Order order, int total, Integer seasonId){
        List<Team> teams = espn.getTeamsSorted(order, total, seasonId);
        String timePeriod = seasonId == null ? " All Time" : " " + String.valueOf(seasonId);

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(total + " Records" + timePeriod + ":\\n");

        for(int i=0; i < teams.size(); i++){
            String optionalYear = seasonId == null ? "(" + teams.get(i).getSeasonId() + ")" : "";
            Member member = espn.getMemberByTeamId(teams.get(i).getId(), teams.get(i).getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            sb.append(i+1 + ": " + memberName + " ")
              .append(teams.get(i).getRecord().getOverall().getWins() + "-" + teams.get(i).getRecord().getOverall().getLosses() + " ")
              .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPercentage()) + " ")
              .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsFor()) + " ")
              .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsAgainst()) + " ")
              .append(optionalYear + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays all jujus of all-time.
     *
     *  a Juju is a week in which a fantasy football team:
     *          1) has a bottom-five score for the week
     *          2) is below the average score for that week
     *          3) wins
     */
    public String buildJujusMessage(){
        return buildJujusMessage(null);
    }

    /***
     *  Builds message that displays all jujus.
     *  TODO: implement buildJujusMessage
     *
     *  a Juju describes a week in which a fantasy football team:
     *          1) has a bottom-five score for the week
     *          2) is below the average score for that week
     *          3) wins
     */
    public String buildJujusMessage(Integer seasonId){
        return NOPE;
    }

    /***
     *  Builds message that displays all salties of all-time.
     *
     *  a Salty describes a week in which a fantasy football team:
     *          1) has a top-five score for the week
     *          2) is above the average score for the week
     *          3) loses
     */
    public String buildSaltiesMessage(){
        return buildSaltiesMessage(null);
    }

    /***
     *  Builds message that displays all salties.
     *  TODO: implement buildSaltiesMessage
     *
     *  a Salty describes a week in which a fantasy football team:
     *          1) has a top-five score for the week
     *          2) is above the average score for the week
     *          3) loses
     */
    public String buildSaltiesMessage(Integer seasonId){
        return NOPE;
    }

    /***
     *  Builds message that displays the biggest blowout matchups of all-time.
     */
    public String buildBlowoutsMessage(int total){
        return buildBlowoutsMessage(total, null);
    }

    /***
     *  Builds message that displays the biggest blowout matchups.
     */
    public String buildBlowoutsMessage(int total, Integer seasonId){
        List<Matchup> matchups = espn.getMatchupsSorted(Order.DESC, total, seasonId, false, false);

        StringBuilder sb = new StringBuilder();
            sb.append("Biggest Blowouts:\\n");
            for(int i=0; i < matchups.size(); i++){
                ScheduleItem.Residence winner = matchups.get(i).getScheduleItem().getWinner().equals("HOME") ? matchups.get(i).getScheduleItem().getHome() : matchups.get(i).getScheduleItem().getAway();
                ScheduleItem.Residence loser = matchups.get(i).getScheduleItem().getWinner().equals("AWAY") ? matchups.get(i).getScheduleItem().getHome() : matchups.get(i).getScheduleItem().getAway();

                Member winnerMember = espn.getMemberByTeamId(winner.getTeamId(), matchups.get(i).getSeasonId());
                String winnerName = winnerMember.getFirtName() + " " + winnerMember.getLastName();

                Member loserMember = espn.getMemberByTeamId(loser.getTeamId(), matchups.get(i).getSeasonId());
                String loserName = loserMember.getFirtName() + " " + loserMember.getLastName();

                sb.append(i+1 + ": " + String.format("%.2f", Math.abs(matchups.get(i).getScheduleItem().getHome().getTotalPoints() - matchups.get(i).getScheduleItem().getAway().getTotalPoints())) + ": ")
                  .append(winnerName + " " + winner.getTotalPoints() + " - ")
                  .append(loser.getTotalPoints() + " " + loserName + " ")
                  .append("(" + matchups.get(i).getScheduleItem().getMatchupPeriodId() + "/")
                  .append(matchups.get(i).getSeasonId() + ")\\n");
            }

        return sb.toString();
    }

    /***
     *  Builds message that displays the closest matchups of all-time.
     */
    public String buildHeartbreaksMessage(int total){
        return buildHeartbreaksMessage(total, null);
    }

    /***
     *  Builds message that displays the closest matchups.
     */
    public String buildHeartbreaksMessage(int total, Integer seasonId){
        List<Matchup> matchups = espn.getMatchupsSorted(Order.ASC, total, seasonId, false, false);

        StringBuilder sb = new StringBuilder();
        sb.append("Biggest Heartbreaks:\\n");
        for(int i=0; i < matchups.size(); i++){
            ScheduleItem.Residence winner = matchups.get(i).getScheduleItem().getWinner().equals("HOME") ? matchups.get(i).getScheduleItem().getHome() : matchups.get(i).getScheduleItem().getAway();
            ScheduleItem.Residence loser = matchups.get(i).getScheduleItem().getWinner().equals("AWAY") ? matchups.get(i).getScheduleItem().getHome() : matchups.get(i).getScheduleItem().getAway();

            Member winnerMember = espn.getMemberByTeamId(winner.getTeamId(), matchups.get(i).getSeasonId());
            String winnerName = winnerMember.getFirtName() + " " + winnerMember.getLastName();

            Member loserMember = espn.getMemberByTeamId(loser.getTeamId(), matchups.get(i).getSeasonId());
            String loserName = loserMember.getFirtName() + " " + loserMember.getLastName();

            sb.append(i+1 + ": " + String.format("%.2f", Math.abs(matchups.get(i).getScheduleItem().getHome().getTotalPoints() - matchups.get(i).getScheduleItem().getAway().getTotalPoints())) + ": ")
                    .append(loserName + " " + loser.getTotalPoints() + " - ")
                    .append(winner.getTotalPoints() + " " + winnerName + " ")
                    .append("(" + matchups.get(i).getScheduleItem().getMatchupPeriodId() + "/")
                    .append(matchups.get(i).getSeasonId() + ")\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the top/bottom points through a given week.
     *  TODO: implement buildPoinstThroughMessage
     */
    public String buildPointsThroughMessage(Order order, int total, int week){
        return NOPE;
    }

    /***
     *  Builds message that displays the top streak of >= given points.
     *  TODO: implement buildPointsStreakMessage
     */
    public String buildPointsStreakMessage(int total){
        return NOPE;
    }
}
