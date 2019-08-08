package ncollins.espn;

import ncollins.model.Order;
import ncollins.model.espn.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Espn {
    private static final String LEAGUE_YEAR = System.getenv("ESPN_LEAGUE_YEAR");
    private League league;

    public Espn() {
        this.league = new EspnDataLoader().loadLeague();
    }

    public League getLeague(){
        return this.league;
    }

    public int getCurrentWeek(){
        return getLeague().getSeason(getCurrentSeasonId()).getScoringPeriodId();
    }

    public int getCurrentSeasonId() {
        return Integer.valueOf(LEAGUE_YEAR);
    }

    public List<Score> getScoresSorted(Order order, int total, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = seasonId != null ?
                getScores(seasonId, includePlayoffs) :
                getScoresAllTime(includePlayoffs);

        scores.sort(new SortByScore(order));

        return scores.subList(0, total);
    }

    public List<Score> getScoresAllTime(boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();

        for(Integer seasonId : getLeague().getSeasons().keySet()){
            scores.addAll(getScores(seasonId, includePlayoffs));
        }

        return scores;
    }

    public List<Score> getScores(Integer seasonId, boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();
        for(ScheduleItem scheduleItem : getLeague().getSeason(seasonId).getSchedule()){
            if(includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")){
                scores.add(new Score(scheduleItem.getHome(), scheduleItem.getAway(), scheduleItem.getMatchupPeriodId(), seasonId));
                scores.add(new Score(scheduleItem.getAway(), scheduleItem.getHome(), scheduleItem.getMatchupPeriodId(), seasonId));
            }
        }
        return scores;
    }

    public List<Matchup> getMatchupsSorted(Order order, int total, Integer seasonId, boolean includePlayoffs, boolean includeTies){
        List<Matchup> matchups = seasonId != null ?
                getMatchups(seasonId, includePlayoffs, includeTies) :
                getMatchupsAllTime(includePlayoffs, includeTies);
        matchups.sort(new SortByDifference(order));

        return matchups.subList(0, total);
    }

    public List<Matchup> getMatchups(Integer seasonId, boolean includePlayoffs, boolean includeTies){
        List<Matchup> matchups = new ArrayList();
        for(ScheduleItem scheduleItem : getLeague().getSeason(seasonId).getSchedule()){
            if(includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")){
                if(includeTies || !scheduleItem.getHome().getTotalPoints().equals(scheduleItem.getAway().getTotalPoints())){
                    matchups.add(new Matchup(scheduleItem, seasonId));
                }
            }
        }

        return matchups;
    }

    public List<Matchup> getMatchupsAllTime(boolean includePlayoffs, boolean includeTies){
        ArrayList<Matchup> matchups = new ArrayList();

        for(Integer seasonId : getLeague().getSeasons().keySet()){
            matchups.addAll(getMatchups(seasonId, includePlayoffs, includeTies));
        }

        return matchups;
    }

    public List<Team> getTeamsSorted(Order order, Integer total, Integer seasonId){
        List<Team> teams = seasonId != null ?
                getTeams(seasonId) :
                getTeamsAllTime();
        teams.sort(new SortByWins(order).thenComparing(new SortByPoints(order)));

        return total != null ? teams.subList(0, total) : teams;
    }

    public List<Team> getTeams(Integer seasonId){
        List<Team> teams = getLeague().getSeason(seasonId).getTeams();

        for(Team team : teams){
            team.setSeasonId(seasonId);
        }

        return teams;
    }

    public List<Team> getTeamsAllTime(){
        ArrayList<Team> teams = new ArrayList();

        for(Integer seasonId : getLeague().getSeasons().keySet()){
            teams.addAll(getTeams(seasonId));
        }

        return teams;
    }

    public Team getTeam(Integer teamId, Integer seasonId){
        for(Team team : getLeague().getSeason(seasonId).getTeams()){
            if(team.getId() == teamId){
                team.setSeasonId(seasonId);
                return team;
            }
        }

        return null;
    }

    public String getTeamAbbrev(Integer teamId, Integer seasonId){
        return getTeam(teamId, seasonId).getAbbrev();
    }

    public Member getMemberByTeamId(Integer teamId, Integer seasonId){
        Team team = getTeam(teamId, seasonId);

        for(Member member : getLeague().getSeason(seasonId).getMembers()){
            if(member.getId().equals(team.getPrimaryOwner())){
                return member;
            }
        }

        return null;
    }

    class SortByScore implements Comparator<Score> {
        private Order order;

        public SortByScore(Order order){ this.order = order; }

        public int compare(Score a, Score b)
        {
            if(this.order.equals(Order.ASC)){
                return a.getPoints().compareTo(b.getPoints());
            } else {
                return b.getPoints().compareTo(a.getPoints());
            }

        }
    }

    class SortByDifference implements Comparator<Matchup> {
        private Order order;

        public SortByDifference(Order order){ this.order = order; }

        public int compare(Matchup a, Matchup b)
        {
            if(this.order.equals(Order.ASC)){
                return Double.valueOf(Math.abs(a.getScheduleItem().getHome().getTotalPoints() - a.getScheduleItem().getAway().getTotalPoints())).compareTo(
                        Double.valueOf(Math.abs(b.getScheduleItem().getHome().getTotalPoints() - b.getScheduleItem().getAway().getTotalPoints())));
            } else {
                return Double.valueOf(Math.abs(b.getScheduleItem().getHome().getTotalPoints() - b.getScheduleItem().getAway().getTotalPoints())).compareTo(
                        Double.valueOf(Math.abs(a.getScheduleItem().getHome().getTotalPoints() - a.getScheduleItem().getAway().getTotalPoints())));
            }

        }
    }

    class SortByWins implements Comparator<Team> {
        private Order order;

        public SortByWins(Order order){ this.order = order; }

        public int compare(Team a, Team b)
        {
            if(this.order.equals(Order.ASC)){
                return a.getRecord().getOverall().getPercentage().compareTo(
                        b.getRecord().getOverall().getPercentage());
            } else {
                return b.getRecord().getOverall().getPercentage().compareTo(
                        a.getRecord().getOverall().getPercentage());
            }
        }
    }

    class SortByPoints implements Comparator<Team> {
        private Order order;

        public SortByPoints(Order order){ this.order = order; }

        public int compare(Team a, Team b)
        {

            if(this.order.equals(Order.ASC)){
                return a.getRecord().getOverall().getPointsFor().compareTo(
                        b.getRecord().getOverall().getPointsFor());
            } else {
                return b.getRecord().getOverall().getPointsFor().compareTo(
                        a.getRecord().getOverall().getPointsFor());
            }
        }
    }
}
