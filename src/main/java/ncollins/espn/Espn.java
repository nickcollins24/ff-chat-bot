package ncollins.espn;

import ncollins.espn.comparators.SortByDifference;
import ncollins.espn.comparators.SortByPoints;
import ncollins.espn.comparators.SortByScore;
import ncollins.espn.comparators.SortByPercentage;
import ncollins.model.Order;
import ncollins.model.espn.*;

import java.util.ArrayList;
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

    public int getWeek(Integer seasonId){
        return getLeague().getSeason(seasonId).getScoringPeriodId();
    }

    public int getCurrentSeasonId() {
        return Integer.valueOf(LEAGUE_YEAR);
    }

    public List<Score> getScoresSorted(Order order, int total, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = seasonId != null ?
                getScores(seasonId, includePlayoffs) :
                getScoresAllTime(includePlayoffs);

        scores.sort(new SortByScore(order));

        return scores.subList(0, Math.min(scores.size(), total));
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

        Season season = getLeague().getSeason(seasonId);
        for(ScheduleItem scheduleItem : season.getSchedule()){
            if(isValidWeek(includePlayoffs, scheduleItem, season.getSeasonId())){
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

        return matchups.subList(0, Math.min(matchups.size(), total));
    }

    public List<Matchup> getMatchups(Integer seasonId, boolean includePlayoffs, boolean includeTies){
        List<Matchup> matchups = new ArrayList();

        Season season = getLeague().getSeason(seasonId);
        for(ScheduleItem scheduleItem : season.getSchedule()){
            if(isValidWeek(includePlayoffs, scheduleItem, season.getSeasonId())){
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

    public List<Team> getTeamsSorted(Order order, Integer total, Integer seasonId, Boolean includeCurrentSeason){
        List<Team> teams = seasonId != null ?
                getTeams(seasonId) :
                getTeamsAllTime(includeCurrentSeason);
        teams.sort(new SortByPercentage(order).thenComparing(new SortByPoints(order)));

        return total != null ? teams.subList(0, Math.min(teams.size(), total)) : teams;
    }

    public List<Team> getTeams(Integer seasonId){
        List<Team> teams = getLeague().getSeason(seasonId).getTeams();

        for(Team team : teams){
            team.setSeasonId(seasonId);
        }

        return teams;
    }

    public List<Team> getTeamsAllTime(Boolean includeCurrentSeason){
        ArrayList<Team> teams = new ArrayList();

        for(Integer seasonId : getLeague().getSeasons().keySet()){
            if(includeCurrentSeason || seasonId < getCurrentSeasonId()){
                teams.addAll(getTeams(seasonId));
            }
        }

        return teams;
    }

    public String getTeamAbbrev(Integer teamId, Integer seasonId){
        return getTeam(teamId, seasonId).getAbbrev();
    }

    public Member getMemberByOwnerId(String ownerId){
        for(Season s : getLeague().getSeasons().values()){
            for(Member m : s.getMembers()){
                if(ownerId.equals(m.getId())){
                    return m;
                }
            }
        }

        return null;
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

    private Team getTeam(Integer teamId, Integer seasonId){
        for(Team team : getLeague().getSeason(seasonId).getTeams()){
            if(team.getId() == teamId){
                team.setSeasonId(seasonId);
                return team;
            }
        }

        return null;
    }

    private Boolean isValidWeek(Boolean includePlayoffs, ScheduleItem scheduleItem, Integer seasonId){
        return (includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")) &&
                scheduleItem.getMatchupPeriodId() < getWeek(seasonId);
    }
}
