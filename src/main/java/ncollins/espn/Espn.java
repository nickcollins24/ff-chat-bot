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

    public List<Score> getScores(Order order, int total){
        List<Score> scores = getScores();
        scores.sort(new SortByScore(order));

        return scores.subList(0, total);
    }

    public List<Score> getScores(){
        ArrayList<Score> scores = new ArrayList();
        for(ScheduleItem scheduleItem : getLeague().getSchedule()){
            if(scheduleItem.getPlayoffTierType().equals("NONE")){
                scores.add(new Score(scheduleItem.getHome(), scheduleItem.getAway(), scheduleItem.getMatchupPeriodId()));
                scores.add(new Score(scheduleItem.getAway(), scheduleItem.getHome(), scheduleItem.getMatchupPeriodId()));
            }
        }
        return scores;
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
