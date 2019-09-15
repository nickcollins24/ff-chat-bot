package ncollins.espn;

import ncollins.espn.comparators.SortOverallByPercentage;
import ncollins.model.Order;
import ncollins.model.espn.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EspnMessageBuilder {
    private Espn espn;

    public EspnMessageBuilder(Espn espn){
        this.espn = espn;
    }

    private static final String NOPE = "i can't do that yet.";

    /***
     *  Builds message that displays the best/worst scores by a team all-time
     */
    public String buildScoresMessage(Order order, int total, boolean includePlayoffs){
        return buildScoresMessage(order, total, null, null, includePlayoffs);
    }

    /***
     *  Builds message that displays the best/worst scores by a team this year
     */
    public String buildScoresMessageCurrentYear(Order order, int total, boolean includePlayoffs){
        return buildScoresMessage(order, total, null, espn.getCurrentSeasonId(), includePlayoffs);
    }

    /***
     *  Builds message that displays the best/worst scores by a team
     */
    public String buildScoresMessage(Order order, int total, Integer scoringPeriodId, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = espn.getScoresSorted(order, total, scoringPeriodId, seasonId, includePlayoffs);
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
     *  Builds message that displays the most/least points by a fantasy player in a week
     */
    public String buildPlayersMessageByWeek(Order order, int total, int week, Position position){
        List<RosterForCurrentScoringPeriod.RosterEntry> players =
                espn.getPlayersByWeeklyPF(order, total, week, position);
        String positionStr = position == null ? "Player" : position.name();

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(players.size() + " Rostered " + positionStr + " of Week " + week + ":\\n");

        for(int i=0; i < players.size(); i++){
            String playerName = espn.getPlayer(players.get(i).getPlayerPoolEntry().getId()).getFullName();
            String benchStatus = players.get(i).getLineupSlotId() == 20 ? " *" : "";
                    sb.append(i+1 + ": " + playerName + "(" + espn.getPositionById(players.get(i).getPlayerPoolEntry().getPlayer().getDefaultPositionId()) + ") - "
                    + String.format("%.2f", players.get(i).getPlayerPoolEntry().getAppliedStatTotal()) + benchStatus + "\\n");
        }
        sb.append("\\n(* = benched)");

        return sb.toString();
    }

    /***
     *  Builds message that displays the most/least points by a fantasy player in current week
     */
    public String buildPlayersMessageByCurrentWeek(Order order, int total, Position position){
        return buildPlayersMessageByWeek(order, total, espn.getCurrentScoringPeriodId(), position);
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
     */
    public String buildMatchupsMessage(String team0, String team1){
        Member m0 = espn.getMemberByTeamAbbrev(team0);
        Team t0 = espn.getTeamByAbbrev(team0);
        Member m1 = espn.getMemberByTeamAbbrev(team1);
        Team t1 = espn.getTeamByAbbrev(team1);

        if(m0 == null || m1 == null || t0 == null || t1 == null){
            return "Team(s) don't exist.";
        }

        Map<Integer, String> teamMap = new HashMap();
        teamMap.put(t0.getId(), team0.toUpperCase());
        teamMap.put(t1.getId(), team1.toUpperCase());

        List<Matchup> matchups = espn.getMatchupsBetweenSorted(Order.DESC, m0, m1);

        if(matchups.isEmpty()){
            return "Teams have never played each other.";
        }

        Double t0Points = 0.0;
        Double t1Points = 0.0;

        int t0Wins = 0;
        int t0Losses = 0;
        int t0Ties = 0;
        for(Matchup m : matchups){
            if(teamMap.get(m.getScheduleItem().getHome().getTeamId()).equalsIgnoreCase(team0)){
                t0Points += m.getScheduleItem().getHome().getTotalPoints();
                t1Points += m.getScheduleItem().getAway().getTotalPoints();

                if(m.getScheduleItem().getHome().getTotalPoints() > m.getScheduleItem().getAway().getTotalPoints()){
                    t0Wins++;
                } else if(m.getScheduleItem().getAway().getTotalPoints() > m.getScheduleItem().getHome().getTotalPoints()){
                    t0Losses++;
                } else {
                    t0Ties++;
                }
            } else {
                t1Points += m.getScheduleItem().getHome().getTotalPoints();
                t0Points += m.getScheduleItem().getAway().getTotalPoints();

                if(m.getScheduleItem().getAway().getTotalPoints() > m.getScheduleItem().getHome().getTotalPoints()){
                    t0Wins++;
                } else if(m.getScheduleItem().getHome().getTotalPoints() > m.getScheduleItem().getAway().getTotalPoints()){
                    t0Losses++;
                } else {
                    t0Ties++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(m0.getDisplayName() + " (" + team0.toUpperCase() + ") vs. " + m1.getDisplayName() + " (" + team1.toUpperCase() + "):\\n\\n");

        sb.append("Record:\\n");
        sb.append(team0.toUpperCase() + ": " + t0Wins + "-" + t0Losses + "-" + t0Ties + " " + String.format("%.2f", t0Points) + " (" + String.format("%.2f", t0Points/matchups.size()) + "/g)\\n")
          .append(team1.toUpperCase() + ": " + t0Losses + "-" + t0Wins + "-" + t0Ties + " " + String.format("%.2f", t1Points) + " (" + String.format("%.2f", t1Points/matchups.size()) + "/g)\\n\\n");

        sb.append("Matchups:\\n");
        for(int i=0; i < matchups.size(); i++){
            String winner;
            String loser;
            if(matchups.get(i).getScheduleItem().getHome().getTotalPoints() >
                    matchups.get(i).getScheduleItem().getAway().getTotalPoints()){
                winner = teamMap.get(matchups.get(i).getScheduleItem().getHome().getTeamId()) + " " +
                        String.format("%.2f", matchups.get(i).getScheduleItem().getHome().getTotalPoints());
                loser = String.format("%.2f", matchups.get(i).getScheduleItem().getAway().getTotalPoints())+ " " +
                        teamMap.get(matchups.get(i).getScheduleItem().getAway().getTeamId());
            } else {
                winner = teamMap.get(matchups.get(i).getScheduleItem().getAway().getTeamId()) + " " +
                        String.format("%.2f", matchups.get(i).getScheduleItem().getAway().getTotalPoints());
                loser = String.format("%.2f", matchups.get(i).getScheduleItem().getHome().getTotalPoints())+ " " +
                        teamMap.get(matchups.get(i).getScheduleItem().getHome().getTeamId());
            }

            sb.append(winner + " - " + loser + " -- ")
              .append(matchups.get(i).getSeasonId() + "(" + matchups.get(i).getScheduleItem().getMatchupPeriodId() + ")\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays current standings
     */
    public String buildStandingsMessageCurrentYear(){
        return buildStandingsMessage(espn.getCurrentSeasonId());
    }

    /**
     * Builds message that displays standings all-time
     */
    public String buildStandingsMessage(){
        List<Team> teams = espn.getTeamsAllTime(true);
        List<OwnerToOverall> recordsByOwner = new ArrayList();

        for(Team t : teams){
            Member member = espn.getMemberByOwnerId(t.getPrimaryOwner());
            String ownerName = member.getFirtName() + " " + member.getLastName();
            OwnerToOverall recordWithOwner = espn.getRecordWithOwner(recordsByOwner, t.getPrimaryOwner(), ownerName);

            if(recordWithOwner != null){
                // update overall wins for team with owner
                recordWithOwner.getOverall().setWins(
                        recordWithOwner.getOverall().getWins() + t.getRecord().getOverall().getWins());
                // update overall losses for team with owner
                recordWithOwner.getOverall().setLosses(
                        recordWithOwner.getOverall().getLosses() + t.getRecord().getOverall().getLosses());
                // update overall ties for team with owner
                recordWithOwner.getOverall().setTies(
                        recordWithOwner.getOverall().getTies() + t.getRecord().getOverall().getTies());
                // update overall percentage for team with owner
                recordWithOwner.getOverall().setPercentage(
                        (recordWithOwner.getOverall().getWins() + (recordWithOwner.getOverall().getTies()*.5)) /
                        (recordWithOwner.getOverall().getWins() + recordWithOwner.getOverall().getLosses() + recordWithOwner.getOverall().getTies()));
                // update overall pf for team with owner
                recordWithOwner.getOverall().setPointsFor(
                        recordWithOwner.getOverall().getPointsFor() + t.getRecord().getOverall().getPointsFor());
                // update overall pa for team with owner
                recordWithOwner.getOverall().setPointsAgainst(
                        recordWithOwner.getOverall().getPointsAgainst() + t.getRecord().getOverall().getPointsAgainst());
            } else {
                Record.Overall overall = new Record().new Overall();
                overall.setWins(t.getRecord().getOverall().getWins());
                overall.setLosses(t.getRecord().getOverall().getLosses());
                overall.setTies(t.getRecord().getOverall().getTies());
                overall.setPercentage(t.getRecord().getOverall().getPercentage());
                overall.setPointsFor(t.getRecord().getOverall().getPointsFor());
                overall.setPointsAgainst(t.getRecord().getOverall().getPointsAgainst());

                recordsByOwner.add(new OwnerToOverall(t.getPrimaryOwner(), ownerName, overall));
            }
        }
        recordsByOwner.sort(new SortOverallByPercentage(Order.DESC));

        StringBuilder sb = new StringBuilder();
        sb.append("All-Time Standings:\\n");
        for(int i=0; i < recordsByOwner.size(); i++){
            String tiesStr = recordsByOwner.get(i).getOverall().getTies() > 0 ?
                    "-" + recordsByOwner.get(i).getOverall().getTies():
                    "";

            sb.append(recordsByOwner.get(i).getOwnerName() + " ")
                    .append(recordsByOwner.get(i).getOverall().getWins() + "-" + recordsByOwner.get(i).getOverall().getLosses() + tiesStr + " ")
                    .append(String.format("%.3f", recordsByOwner.get(i).getOverall().getPercentage()) + " ")
                    .append(String.format("%.1f", recordsByOwner.get(i).getOverall().getPointsFor()) + " ")
                    .append(String.format("%.1f", recordsByOwner.get(i).getOverall().getPointsAgainst()) + "\\n");
        }

        return sb.toString();
    }

    /**
     * Builds message that displays standings in a given year
     */
    public String buildStandingsMessage(Integer seasonId){
        List<Team> teams = espn.getTeamsSorted(Order.DESC, null, seasonId, true);

        StringBuilder sb = new StringBuilder();
        sb.append(seasonId + " Standings:\\n");
        for(int i=0; i < teams.size(); i++){
            Member member = espn.getMemberByTeamId(teams.get(i).getId(), teams.get(i).getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            String tiesStr = teams.get(i).getRecord().getOverall().getTies() > 0 ?
                    "-" + teams.get(i).getRecord().getOverall().getTies():
                    "";

            sb.append(memberName + " ")
                    .append(teams.get(i).getRecord().getOverall().getWins() + "-" + teams.get(i).getRecord().getOverall().getLosses() + tiesStr + " ")
                    .append(String.format("%.3f", teams.get(i).getRecord().getOverall().getPercentage()) + " ")
                    .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsFor()) + " ")
                    .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsAgainst()) + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the best/worst records by a team this year
     */
    public String buildRecordsMessageCurrentYear(Order order, int total){
        return buildRecordsMessage(order, total, espn.getCurrentSeasonId(), true);
    }

    /***
     *  Builds message that displays best/worst records all-time
     */
    public String buildRecordsMessage(Order order, int total){
        return buildRecordsMessage(order, total, null, false);
    }

    /***
     *  Builds message that displays best/worst records in given year
     */
    public String buildRecordsMessage(Order order, int total, Integer seasonId){
        return buildRecordsMessage(order, total, seasonId, true);
    }

    /***
     *  Builds message that displays best/worst records in given year
     */
    public String buildRecordsMessage(Order order, int total, Integer seasonId, Boolean includeCurrentSeason){
        List<Team> teams = espn.getTeamsSorted(order, total, seasonId, includeCurrentSeason);
        String timePeriod = seasonId == null ? " All Time" : " " + String.valueOf(seasonId);

        StringBuilder sb = new StringBuilder();
        sb.append(order.equals(Order.ASC) ? "Bottom " : "Top ").append(total + " Records" + timePeriod + ":\\n");

        for(int i=0; i < teams.size(); i++){
            String optionalYear = seasonId == null ? "(" + teams.get(i).getSeasonId() + ")" : "";
            Member member = espn.getMemberByTeamId(teams.get(i).getId(), teams.get(i).getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            String tiesStr = teams.get(i).getRecord().getOverall().getTies() > 0 ?
                "-" + teams.get(i).getRecord().getOverall().getTies():
                "";

            sb.append(i+1 + ": " + memberName + " ")
              .append(teams.get(i).getRecord().getOverall().getWins() + "-" + teams.get(i).getRecord().getOverall().getLosses() + tiesStr + " ")
              .append(String.format("%.3f", teams.get(i).getRecord().getOverall().getPercentage()) + " ")
              .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsFor()) + " ")
              .append(String.format("%.1f", teams.get(i).getRecord().getOverall().getPointsAgainst()) + " ")
              .append(optionalYear + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays all jujus.
     *
     *  a Juju describes a week in which a fantasy football team:
     *          1) has a bottom-five score for the week
     *          2) is below the average score for that week
     *          3) wins
     */
    public String buildJujusMessage(){
        List<Score> jujus = espn.getJujus(espn.getCurrentSeasonId());

        if(jujus.isEmpty()){
            return "no jujus yet this year.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Jujus of " + espn.getCurrentSeasonId() + ":\\n");

        for(Score juju : jujus){
            Member member = espn.getMemberByTeamId(juju.getTeamId(), espn.getCurrentSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            sb.append(memberName + " (")
              .append(String.format("%.1f", juju.getPoints()) + "-")
              .append(String.format("%.1f", juju.getPointsAgainst()) + ") - ")
              .append("wk " + juju.getMatchupPeriodId() + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays all salties.
     *
     *  a Salty describes a week in which a fantasy football team:
     *          1) has a top-five score for the week
     *          2) is above the average score for the week
     *          3) loses
     */
    public String buildSaltiesMessage(){
        List<Score> salties = espn.getSalties(espn.getCurrentSeasonId());

        if(salties.isEmpty()){
            return "no salties yet this year.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Salties of " + espn.getCurrentSeasonId() + ":\\n");

        for(Score salty : salties){
            Member member = espn.getMemberByTeamId(salty.getTeamId(), espn.getCurrentSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            sb.append(memberName + " (")
                    .append(String.format("%.1f", salty.getPoints()) + "-")
                    .append(String.format("%.1f", salty.getPointsAgainst()) + ") - ")
                    .append("wk " + salty.getMatchupPeriodId() + "\\n");
        }

        return sb.toString();
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

    /***
     *  Builds message that displays info from previous week including:
     *      - highest pf
     *      - jujus TODO
     *      - salties TODO
     *      - power ranking update TODO
     */
    public String buildWeeklyRoundupMessage(){
        Integer previousWeek = espn.getCurrentScoringPeriodId()-1;
        List<Score> scores = espn.getScoresSorted(Order.DESC, 1, previousWeek, espn.getCurrentSeasonId(), false);
        Score topScore = scores.get(0);
        Member member = espn.getMemberByTeamId(topScore.getTeamId());

        StringBuilder sb = new StringBuilder();
        sb.append("Week " + previousWeek + " High Score \uD83D\uDCB0:\\n")
          .append(member.getFirtName() + " " + member.getLastName() + " (" + topScore.getPoints() + " PF)");

        return sb.toString();
    }
}
