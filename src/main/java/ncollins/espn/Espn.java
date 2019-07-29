package ncollins.espn;

import ncollins.model.Order;
import ncollins.model.espn.League;
import ncollins.model.espn.ScheduleItem;
import ncollins.model.espn.Score;
import ncollins.model.espn.Team;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Espn {
    private League league;

    public Espn() {
        this.league = new EspnDataLoader().loadLeague();
    }

    public League getLeague(){
        return this.league;
    }

    public List<Score> getScoresSorted(Order order, int total, boolean includePlayoffs){
        List<Score> scores = getScores(includePlayoffs);
        scores.sort(new SortByScore(order));

        return scores.subList(0, total);
    }

    public List<Score> getScores(boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();
        for(ScheduleItem scheduleItem : getLeague().getSchedule()){
            if(includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")){
                scores.add(new Score(scheduleItem.getHome(), scheduleItem.getAway(), scheduleItem.getMatchupPeriodId()));
                scores.add(new Score(scheduleItem.getAway(), scheduleItem.getHome(), scheduleItem.getMatchupPeriodId()));
            }
        }
        return scores;
    }

    public List<ScheduleItem> getMatchupsSorted(Order order, int total, boolean includePlayoffs){
        List<ScheduleItem> matchups = getMatchupsSorted(includePlayoffs);
        matchups.sort(new SortByDifference(order));

        return matchups.subList(0, total);
    }

    public List<ScheduleItem> getMatchupsSorted(boolean includePlayoffs){
        List<ScheduleItem> matchups = new ArrayList();
        for(ScheduleItem scheduleItem : getLeague().getSchedule()){
            if(includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")){
                matchups.add(scheduleItem);
            }
        }

        return matchups;
    }

    public List<Team> getTeams(){
        List<Team> teams = getLeague().getTeams();
        teams.sort(new SortByWins().thenComparing(new SortByPoints()));

        return teams;
    }

    public String getTeamAbbrev(int teamId){
        for(Team team : getLeague().getTeams()){
            if(team.getId() == teamId){
                return team.getAbbrev();
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

    class SortByDifference implements Comparator<ScheduleItem> {
        private Order order;

        public SortByDifference(Order order){ this.order = order; }

        public int compare(ScheduleItem a, ScheduleItem b)
        {
            if(this.order.equals(Order.ASC)){
                return Double.valueOf(Math.abs(a.getHome().getTotalPoints() - a.getAway().getTotalPoints())).compareTo(
                        Double.valueOf(Math.abs(b.getHome().getTotalPoints() - b.getAway().getTotalPoints())));
            } else {
                return Double.valueOf(Math.abs(b.getHome().getTotalPoints() - b.getAway().getTotalPoints())).compareTo(
                        Double.valueOf(Math.abs(a.getHome().getTotalPoints() - a.getAway().getTotalPoints())));
            }

        }
    }

    class SortByWins implements Comparator<Team> {
        public int compare(Team a, Team b)
        {
            return b.getRecord().getOverall().getPercentage().compareTo(
                    a.getRecord().getOverall().getPercentage());
        }
    }

    class SortByPoints implements Comparator<Team> {
        public int compare(Team a, Team b)
        {
            return b.getRecord().getOverall().getPointsFor().compareTo(
                    a.getRecord().getOverall().getPointsFor());
        }
    }
}
