package ncollins.espn;

import ncollins.espn.comparators.*;
import ncollins.model.Order;
import ncollins.model.espn.*;

import java.util.*;

public class Espn {
    private static final String LEAGUE_YEAR = System.getenv("ESPN_LEAGUE_YEAR");
    private League league;
    private EspnDataLoader loader = new EspnDataLoader();

    public Espn() {
        this.league = loader.loadLeague();
    }

    public League getLeague(){
        return league;
    }

    public Season getSeason(Integer seasonId, boolean live){
        return live ? loader.loadSeason(seasonId) : getLeague().getSeason(seasonId);
    }

    public Map<Integer,Season> getSeasons(){
        return getLeague().getSeasons();
    }

    public int getWeek(Integer seasonId){
        return getSeason(seasonId, false).getScoringPeriodId();
    }

    public int getCurrentSeasonId() {
        return Integer.valueOf(LEAGUE_YEAR);
    }

    public List<Score> getScoresSorted(Order order, int total, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = seasonId != null ?
                getScores(seasonId, includePlayoffs) :
                getScoresAllTime(includePlayoffs);

        scores.sort(new SortScoresByPoints(order));

        return scores.subList(0, Math.min(scores.size(), total));
    }

    public List<Score> getScoresAllTime(boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();

        for(Integer seasonId : getSeasons().keySet()){
            scores.addAll(getScores(seasonId, includePlayoffs));
        }

        return scores;
    }

    public List<Score> getScores(Integer seasonId, boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();

        Season season = getSeason(seasonId,false);
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
        matchups.sort(new SortMatchupsByDifference(order));

        return matchups.subList(0, Math.min(matchups.size(), total));
    }

    public List<Matchup> getMatchups(Integer seasonId, boolean includePlayoffs, boolean includeTies){
        List<Matchup> matchups = new ArrayList();

        Season season = getSeason(seasonId,false);
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

        for(Integer seasonId : getSeasons().keySet()){
            matchups.addAll(getMatchups(seasonId, includePlayoffs, includeTies));
        }

        return matchups;
    }

    public List<Matchup> getMatchupsBetween(Member m0, Member m1){
        List<Matchup> allMatchups = getMatchupsAllTime(true, true);
        Set<Matchup> matchupsBetween = new HashSet();

        for(Matchup matchup : allMatchups){
            if(matchup.getScheduleItem().getHome() != null && matchup.getScheduleItem().getAway() != null) {
                Member homeMember = getMemberByTeamId(matchup.getScheduleItem().getHome().getTeamId(), matchup.getSeasonId());
                Member awayMember = getMemberByTeamId(matchup.getScheduleItem().getAway().getTeamId(), matchup.getSeasonId());

                if ((homeMember.equals(m0) && awayMember.equals(m1)) ||
                        (homeMember.equals(m1) && awayMember.equals(m0))) {
                    matchupsBetween.add(matchup);
                }
            }
        }

        return new ArrayList(matchupsBetween);
    }

    public List<Matchup> getMatchupsBetweenSorted(Order order, Member m0, Member m1){
        List<Matchup> matchups = getMatchupsBetween(m0, m1);
        matchups.sort(new SortMatchupsBySeasonId(order).thenComparing(new SortMatchupsByWeek(order)));

        return matchups;
    }

    public List<Team> getTeamsSorted(Order order, Integer total, Integer seasonId, Boolean includeCurrentSeason){
        List<Team> teams = seasonId != null ?
                getTeams(seasonId,false) :
                getTeamsAllTime(includeCurrentSeason);
        teams.sort(new SortTeamsByPercentage(order).thenComparing(new SortTeamsByPoints(order)));

        return total != null ? teams.subList(0, Math.min(teams.size(), total)) : teams;
    }

    public List<Team> getTeams(Integer seasonId, boolean live){
        List<Team> teams = getSeason(seasonId, live).getTeams();

        for(Team team : teams){
            team.setSeasonId(seasonId);
        }

        return teams;
    }

    public List<Team> getTeamsAllTime(Boolean includeCurrentSeason){
        ArrayList<Team> teams = new ArrayList();

        for(Integer seasonId : getSeasons().keySet()){
            if(includeCurrentSeason || seasonId < getCurrentSeasonId()){
                teams.addAll(getTeams(seasonId,false));
            }
        }

        return teams;
    }

    public Member getMemberByOwnerId(String ownerId){
        for(Season s : getSeasons().values()){
            for(Member m : s.getMembers()){
                if(ownerId.equals(m.getId())){
                    return m;
                }
            }
        }

        return null;
    }

    public Team getTeamByAbbrev(String abbrev){
        return getTeamByAbbrev(abbrev, getCurrentSeasonId());
    }

    public Team getTeamByAbbrev(String abbrev, Integer seasonId){
        for(Team t : getTeams(seasonId,true)){
            if(t.getAbbrev().equalsIgnoreCase(abbrev)){
                return t;
            }
        }

        return null;
    }

    public Member getMemberByTeamAbbrev(String abbrev){
        return getMemberByTeamAbbrev(abbrev, getCurrentSeasonId());
    }

    public Member getMemberByTeamAbbrev(String abbrev, Integer seasonId){
        for(Team t : getTeams(seasonId,true)){
            if(t.getAbbrev().equalsIgnoreCase(abbrev)){
                return getMemberByTeamId(t.getId(), seasonId);
            }
        }

        return null;
    }

    public Member getMemberByTeamId(Integer teamId){
        return getMemberByTeamId(teamId, this.getCurrentSeasonId());
    }

    public Member getMemberByTeamId(Integer teamId, Integer seasonId){
        Team team = getTeam(teamId, seasonId);

        for(Member member : getSeason(seasonId,false).getMembers()){
            if(member.getId().equals(team.getPrimaryOwner())){
                return member;
            }
        }

        return null;
    }

    public OwnerToOverall getRecordWithOwner(List<OwnerToOverall> ownerToOverall, String ownerId, String ownerName){
        for(OwnerToOverall o : ownerToOverall){
            if(o.getOwnerId().equals(ownerId) || o.getOwnerName().equals(ownerName)){
                return o;
            }
        }

        return null;
    }

    private Team getTeam(Integer teamId, Integer seasonId){
        for(Team team : getSeason(seasonId,false).getTeams()){
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
