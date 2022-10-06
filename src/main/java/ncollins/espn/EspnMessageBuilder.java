package ncollins.espn;

import ncollins.espn.comparators.SortOverallByPercentage;
import ncollins.model.Order;
import ncollins.model.chat.Emojis;
import ncollins.model.espn.*;
import ncollins.model.espn.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EspnMessageBuilder {
    private Espn espn;

    @Autowired
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
     *  Builds message that displays the league loser every year
     */
    public String buildSackosMessage(){
        List<Team> sackos = espn.getSackos();

        StringBuilder sb = new StringBuilder();


        sb.append("Sackos:\\n");
        // hardcoding this here because i dont have access to data from this year
        sb.append("2011: J Mehta\\n");
        for(Team sacko : sackos){
            Member member = espn.getMemberByTeamId(sacko.getId(), sacko.getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            String tiesStr = sacko.getRecord().getOverall().getTies() > 0 ?
                    "-" + sacko.getRecord().getOverall().getTies():
                    "";

            sb.append(sacko.getSeasonId() + ": " + memberName + " ")
                    .append(sacko.getRecord().getOverall().getWins() + "-" + sacko.getRecord().getOverall().getLosses() + tiesStr + " ")
                    .append(String.format("%.1f", sacko.getRecord().getOverall().getPointsFor()) + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays the league winner every year
     */
    public String buildChampsMessage(){
        List<Team> champs = espn.getChamps();

        StringBuilder sb = new StringBuilder();


        sb.append("Champs:\\n");

        // hardcoding this here because i dont have access to data from this year
        sb.append("2011: Andrew Baclig\\n");
        for(Team champ : champs){
            Member member = espn.getMemberByTeamId(champ.getId(), champ.getSeasonId());
            String memberName = member.getFirtName() + " " + member.getLastName();

            String tiesStr = champ.getRecord().getOverall().getTies() > 0 ?
                    "-" + champ.getRecord().getOverall().getTies():
                    "";

            sb.append(champ.getSeasonId() + ": " + memberName + " ")
                    .append(champ.getRecord().getOverall().getWins() + "-" + champ.getRecord().getOverall().getLosses() + tiesStr + " ")
                    .append(String.format("%.1f", champ.getRecord().getOverall().getPointsFor()) + "\\n");
        }

        return sb.toString();
    }

    /***
     *  Builds message that displays matchup history between two teams
     */
    public String buildMatchupsMessage(String teamAbbrev0, String teamAbbrev1){
        Member m0 = espn.getMemberByTeamAbbrev(teamAbbrev0);
        Member m1 = espn.getMemberByTeamAbbrev(teamAbbrev1);

        if(m0 == null || m1 == null){
            return "Team(s) don't exist.";
        }

        Map<String, String> memberMap = new HashMap();
        memberMap.put(m0.getId(), teamAbbrev0.toUpperCase());
        memberMap.put(m1.getId(), teamAbbrev1.toUpperCase());

        List<Matchup> matchups = espn.getMatchupsBetweenSorted(Order.DESC, m0, m1);

        if(matchups.isEmpty()){
            return "Teams have never played each other.";
        }

        Double t0Points = 0.0;
        Double t1Points = 0.0;
        int t0Wins = 0;
        int t0Losses = 0;
        int t0Ties = 0;

        StringBuilder sbMatchup = new StringBuilder();
        for(Matchup m : matchups){
            String winnerStr;
            String loserStr;
            ScheduleItem.Home homeTeam = m.getScheduleItem().getHome();
            ScheduleItem.Away awayTeam = m.getScheduleItem().getAway();
            String homeMemberId = espn.getMemberByTeamId(homeTeam.getTeamId(), m.getSeasonId()).getId();
            String awayMemberId = espn.getMemberByTeamId(awayTeam.getTeamId(), m.getSeasonId()).getId();
            Boolean isHomeTeamMember0 = memberMap.get(homeMemberId).equalsIgnoreCase(teamAbbrev0);

            // tally points
            t0Points += isHomeTeamMember0 ? homeTeam.getTotalPoints() : awayTeam.getTotalPoints();
            t1Points += isHomeTeamMember0 ? awayTeam.getTotalPoints() : homeTeam.getTotalPoints();

            // Home Team wins
            if(homeTeam.getTotalPoints() > awayTeam.getTotalPoints()){
                winnerStr = memberMap.get(homeMemberId) + " " + String.format("%.2f", homeTeam.getTotalPoints());
                loserStr = String.format("%.2f", awayTeam.getTotalPoints()) + " " + memberMap.get(awayMemberId);

                if(isHomeTeamMember0) {
                    t0Wins++;
                } else t0Losses++;
            // Away Team wins
            } else if(awayTeam.getTotalPoints() > homeTeam.getTotalPoints()){
                winnerStr = memberMap.get(awayMemberId) + " " + String.format("%.2f", awayTeam.getTotalPoints());
                loserStr = String.format("%.2f", homeTeam.getTotalPoints()) + " " + memberMap.get(homeMemberId);

                if(isHomeTeamMember0) {
                    t0Losses++;
                } else t0Wins++;
            // Teams tie
            } else {
                winnerStr = memberMap.get(awayMemberId) + " " + String.format("%.2f", awayTeam.getTotalPoints());
                loserStr = String.format("%.2f", homeTeam.getTotalPoints()) + " " + memberMap.get(homeMemberId);

                t0Ties++;
            }

            sbMatchup.append(winnerStr + " - " + loserStr + " -- " + m.getSeasonId() + "(" + m.getScheduleItem().getMatchupPeriodId() + ")\\n");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-- " + m0.getFirtName() + " " + m0.getLastName() + " (" + teamAbbrev0.toUpperCase() + ") vs. " + m1.getFirtName() + " " + m1.getLastName() + " (" + teamAbbrev1.toUpperCase() + ") --\\n\\n");

        sb.append("Record:\\n");
        sb.append(teamAbbrev0.toUpperCase() + ": " + t0Wins + "-" + t0Losses + "-" + t0Ties + " " + String.format("%.2f", t0Points) + " (" + String.format("%.2f", t0Points/matchups.size()) + "/g)\\n")
          .append(teamAbbrev1.toUpperCase() + ": " + t0Losses + "-" + t0Wins + "-" + t0Ties + " " + String.format("%.2f", t1Points) + " (" + String.format("%.2f", t1Points/matchups.size()) + "/g)\\n\\n");

        sb.append("Matchups:\\n");
        sb.append(sbMatchup);

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
     *  Builds message that displays trades in current year
     */
    public String buildTradesMessageCurrentYear(){
        return buildTradesMessage(espn.getCurrentSeasonId());
    }

    /**
     * TODO: add this functionality
     * Builds message that displays trades all-time
     */
    public String buildTradesMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("All-Time Trades:\\n");

        return sb.toString();
    }

    /**
     * TODO: add this functionality
     * Builds message that displays trades in a given year
     */
    public String buildTradesMessage(Integer seasonId){
        StringBuilder sb = new StringBuilder();
        sb.append(seasonId + " Trades:\\n");

        return sb.toString();
    }

    /***
     *  Builds message that displays trades in current year
     */
    public String buildTradeCountMessageCurrentYear(){
        return buildTradeCountMessage(espn.getCurrentSeasonId());
    }

    /**
     * TODO: add this functionality
     * Builds message that displays trades all-time
     */
    public String buildTradeCountMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("All-Time Trade Counts:\\n");

        return sb.toString();
    }

    /**
     * TODO: add this functionality
     * Builds message that displays trades in a given year
     */
    public String buildTradeCountMessage(Integer seasonId){
        StringBuilder sb = new StringBuilder();
        sb.append(seasonId + " Trade Counts:\\n");

        return sb.toString();
    }

    /**
     * TODO: add this functionality
     * Builds message that displays the most common trade partners
     */
    public String buildTradeBaesMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("All-Time Trade Baes:\\n");

        return sb.toString();
    }

    /**
     * Builds message that displays playoff standings ever
     */
    public String buildPlayoffStandingsMessage(){
        List<Matchup> matchups = espn.getPlayoffMatchupsAllTime();
        Map<Member, Record.Overall> memberOverallMap = new HashMap();
        List<OwnerToOverall> recordsByOwner = new ArrayList();

        for(Matchup m : matchups){
            ScheduleItem scheduleItem = m.getScheduleItem();
            Member memberWinner;
            Member memberLoser;

            if(m.getScheduleItem().getWinner().equalsIgnoreCase("HOME")){
                memberWinner = espn.getMemberByTeamId(scheduleItem.getHome().getTeamId(), m.getSeasonId());
                memberLoser = espn.getMemberByTeamId(scheduleItem.getAway().getTeamId(), m.getSeasonId());
            } else {
                memberWinner = espn.getMemberByTeamId(scheduleItem.getAway().getTeamId(), m.getSeasonId());
                memberLoser = espn.getMemberByTeamId(scheduleItem.getHome().getTeamId(), m.getSeasonId());
            }

            // update winner info
            if(memberOverallMap.containsKey(memberWinner)){
                memberOverallMap.get(memberWinner).setWins(
                        memberOverallMap.get(memberWinner).getWins() + 1);
                memberOverallMap.get(memberWinner).setPercentage(
                        (double) memberOverallMap.get(memberWinner).getWins() /
                                (memberOverallMap.get(memberWinner).getWins() + memberOverallMap.get(memberWinner).getLosses())
                );
            } else {
                Record.Overall overallWinner = new Record().new Overall();
                overallWinner.setWins(1);
                overallWinner.setLosses(0);
                overallWinner.setPercentage(1.0);

                memberOverallMap.put(memberWinner, overallWinner);
            }

            // update loser info
            if(memberOverallMap.containsKey(memberLoser)){
                memberOverallMap.get(memberLoser).setLosses(
                        memberOverallMap.get(memberLoser).getLosses() + 1);
                memberOverallMap.get(memberLoser).setPercentage(
                        (double) memberOverallMap.get(memberLoser).getWins() /
                                (memberOverallMap.get(memberLoser).getWins() + memberOverallMap.get(memberLoser).getLosses())
                );
            } else {
                Record.Overall overallLoser = new Record().new Overall();
                overallLoser.setWins(0);
                overallLoser.setLosses(1);
                overallLoser.setPercentage(0.0);

                memberOverallMap.put(memberLoser, overallLoser);
            }

        }

        for(Map.Entry<Member, Record.Overall> e : memberOverallMap.entrySet()){
            String ownerName = e.getKey().getFirtName() + " " + e.getKey().getLastName();
            OwnerToOverall ownerToOverall = new OwnerToOverall(e.getKey().getId(), ownerName, e.getValue());
            recordsByOwner.add(ownerToOverall);
        }
        recordsByOwner.sort(new SortOverallByPercentage(Order.DESC));

        StringBuilder sb = new StringBuilder();
        sb.append("All-Time Playoff Standings:\\n");
        for(int i=0; i < recordsByOwner.size(); i++){
            sb.append(recordsByOwner.get(i).getOwnerName() + " ")
                    .append(recordsByOwner.get(i).getOverall().getWins() + "-" + recordsByOwner.get(i).getOverall().getLosses() + " ")
                    .append(String.format("%.3f", recordsByOwner.get(i).getOverall().getPercentage()) + "\\n");
        }
        sb.append("\\nNote: Standings do not include Consolation or Sacko Brackets.");

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

    public String buildWeeklyPfWinners(){
        StringBuilder sb = new StringBuilder();

        sb.append("High Scores " + Emojis.MONEY_BAG + "\\n");
        for(int i=1; i < espn.getCurrentScoringPeriodId(); i++){
            Score topScore = espn.getScoresSorted(Order.DESC, 1, i, espn.getCurrentSeasonId(), true).get(0);
            Member topScoreMember = espn.getMemberByTeamId(topScore.getTeamId(), espn.getCurrentSeasonId());
            sb.append("Week " + i + ": " + topScoreMember.getFirtName() + " " + topScoreMember.getLastName() + " (" + topScore.getPoints() + ")\\n");
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

//    public String buildOutcomeThroughMessage(Order order, Outcome outcome, int total, int week){
//        List<OwnerToOverall> outcomes = espn.getRecordThroughWeek(order, total, week);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(outcomes.size()).append(order.equals(Order.ASC) ? " Least" : " Most")
//                .append(outcome.equals(Outcome.WIN) ? " Wins" : " Losses")
//                .append(" Through ").append(week + " Weeks");
//
//        for(OwnerToOverall o : outcomes){
//            sb.append(o.getOwnerName() + " " + o.getOverall().getWins() + " " + o.getOverall().getLosses() + " " + o.getOverall().getTies());
//        }
//
//        return sb.toString();
//    }


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
     *      - jujus
     *      - salties
     */
    public String buildWeeklyRoundupMessage(){
        Integer previousWeek = espn.getCurrentScoringPeriodId()-1;
        List<Score> scores = espn.getScoresSorted(Order.DESC, 1, previousWeek, espn.getCurrentSeasonId(), false);
        List<Score> jujus = espn.getJujus(previousWeek, espn.getCurrentSeasonId());
        List<Score> salties = espn.getSalties(previousWeek, espn.getCurrentSeasonId());

        Score topScore = scores.get(0);
        Member topScoreMember = espn.getMemberByTeamId(topScore.getTeamId());

        StringBuilder sb = new StringBuilder();
        sb.append("-- Week " + previousWeek + " Roundup --\\n\\n")
          .append("High Score " + Emojis.MONEY_BAG + ":\\n")
          .append(topScoreMember.getFirtName() + " " + topScoreMember.getLastName() + " (" + topScore.getPoints() + ")\\n\\n");

        sb.append("Jujus:\\n");
        if(jujus.isEmpty()) {
            sb.append("none\\n");
        } else {
            for (Score juju : jujus) {
                Member jujuMember = espn.getMemberByTeamId(juju.getTeamId());
                sb.append(jujuMember.getFirtName() + " " + jujuMember.getLastName() + " (")
                        .append(String.format("%.1f", juju.getPoints()) + "-")
                        .append(String.format("%.1f", juju.getPointsAgainst()) + ")\\n");
            }
        }

        sb.append("\\nSalties:\\n");
        if(salties.isEmpty()){
            sb.append("none\\n");
        } else {
            for(Score salty : salties){
                Member saltyMember = espn.getMemberByTeamId(salty.getTeamId());
                sb.append(saltyMember.getFirtName() + " " + saltyMember.getLastName() + " (")
                        .append(String.format("%.1f", salty.getPoints()) + "-")
                        .append(String.format("%.1f", salty.getPointsAgainst()) + ")\\n");
            }
        }

        return sb.toString();
    }

    public String buildGamedayMessage(){
        List<Matchup> matchups = espn.getMatchups(espn.getCurrentScoringPeriodId(), espn.getCurrentSeasonId());
        StringBuilder sb = new StringBuilder();

        sb.append("Its GameDay! Heres whats on tap " + Emojis.BEER_MUG + " " + Emojis.FOOTBALL + "\\n\\n");
        for(Matchup matchup : matchups){
            Member m0 = espn.getMemberByTeamId(matchup.getScheduleItem().getHome().getTeamId());
            Member m1 = espn.getMemberByTeamId(matchup.getScheduleItem().getAway().getTeamId());
            Map<Member, Record> recordBetween = espn.getRecordBetween(m0,m1);

            sb.append(m0.getFirtName() + " " + m0.getLastName() + " (" + recordBetween.get(m0).getOverall().getWins() + ") vs. " +
                    m1.getFirtName() + " " + m1.getLastName() + " (" + recordBetween.get(m1).getOverall().getWins() + ")\\n");
        }
        sb.append("\\n(x) = career wins vs. opponent");

        return sb.toString();
    }
}
