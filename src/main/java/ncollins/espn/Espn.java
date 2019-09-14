package ncollins.espn;

import ncollins.espn.comparators.*;
import ncollins.model.Order;
import ncollins.model.espn.*;

import java.util.*;

public class Espn {
    private EspnDataLoader loader;

    public Espn(EspnDataLoader loader) {
        this.loader = loader;
    }

    private Season getSeason(Integer seasonId){
        return loader.getSeason(seasonId);
    }

    private Map<Integer,Season> getSeasons(){
        return loader.getSeasons();
    }

    public int getWeek(Season season){
        return season.getScoringPeriodId();
    }

    public int getCurrentSeasonId() {
        return loader.getCurrentSeasonId();
    }

    public int getCurrentScoringPeriodId(){
        return getWeek(loader.getSeason(loader.getCurrentSeasonId()));
    }

    public List<Transaction> getTransactions(long fromDate, long toDate, List<Integer> transactionIds){
        return loader.getTransactions(fromDate, toDate, transactionIds);
    }

    public Player getPlayer(Integer playerId){
        return loader.getPlayer(playerId);
    }

    public List<RosterForCurrentScoringPeriod.PlayerPoolEntry> getPlayersByWeeklyPF(Order order, int total, int week, Position position){
        List<ScheduleItem> scheduleItems = loader.getSeason(getCurrentSeasonId()).getSchedule();
        List<RosterForCurrentScoringPeriod.PlayerPoolEntry> players = new ArrayList();

        for(ScheduleItem item : scheduleItems){
            if(item.getMatchupPeriodId().equals(week)){
                for(RosterForCurrentScoringPeriod.RosterEntry e : item.getHome().getRosterForCurrentScoringPeriod().getRosterEntries()){
                    if(position == null || e.getPlayerPoolEntry().getPlayer().getDefaultPositionId().equals(position.getValue())){
                        players.add(e.getPlayerPoolEntry());
                    }
                }
                for(RosterForCurrentScoringPeriod.RosterEntry e : item.getAway().getRosterForCurrentScoringPeriod().getRosterEntries()){
                    if(position == null || e.getPlayerPoolEntry().getPlayer().getDefaultPositionId().equals(position.getValue())){
                        players.add(e.getPlayerPoolEntry());
                    }
                }
            }
        }

        players.sort(new SortPlayerEntriesByPoints(order));
        return players.subList(0, Math.min(players.size(), total));
    }

    public List<Score> getScoresSorted(Order order, int total, Integer scoringPeriodId, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = seasonId != null ?
                getScores(scoringPeriodId, seasonId, includePlayoffs) :
                getScoresAllTime(scoringPeriodId, includePlayoffs);

        scores.sort(new SortScoresByPoints(order));

        return scores.subList(0, Math.min(scores.size(), total));
    }

    public List<Score> getScoresAllTime(Integer scoringPeriodId, boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();

        for(Integer seasonId : getSeasons().keySet()){
            scores.addAll(getScores(scoringPeriodId, seasonId, includePlayoffs));
        }

        return scores;
    }

    public List<Score> getScores(Integer scoringPeriodId, Integer seasonId, boolean includePlayoffs){
        ArrayList<Score> scores = new ArrayList();

        Season season = getSeason(seasonId);
        for(ScheduleItem scheduleItem : season.getSchedule()){
            if(isValidWeek(includePlayoffs, scheduleItem, season) &&
                    (scoringPeriodId == null || scoringPeriodId.equals(scheduleItem.getMatchupPeriodId()))){
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

        Season season = getSeason(seasonId);
        for(ScheduleItem scheduleItem : season.getSchedule()){
            if(isValidWeek(includePlayoffs, scheduleItem, season)){
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
                getTeams(seasonId) :
                getTeamsAllTime(includeCurrentSeason);
        teams.sort(new SortTeamsByPercentage(order).thenComparing(new SortTeamsByPoints(order)));

        return total != null ? teams.subList(0, Math.min(teams.size(), total)) : teams;
    }

    public List<Team> getTeams(Integer seasonId){
        List<Team> teams = getSeason(seasonId).getTeams();

        for(Team team : teams){
            team.setSeasonId(seasonId);
        }

        return teams;
    }

    public List<Team> getTeamsAllTime(Boolean includeCurrentSeason){
        ArrayList<Team> teams = new ArrayList();

        for(Integer seasonId : getSeasons().keySet()){
            if(includeCurrentSeason || seasonId < getCurrentSeasonId()){
                teams.addAll(getTeams(seasonId));
            }
        }

        return teams;
    }

    public Member getMemberByOwnerId(String ownerId){
        if(ownerId == null || ownerId.isEmpty()){
            return null;
        }

        for(Season s : getSeasons().values()){
            for(Member m : s.getMembers()){
                if(ownerId.equals(m.getId())){
                    return m;
                }
            }
        }

        return null;
    }

    public Team getTeamByOwnerId(String ownerId){
        if(ownerId == null || ownerId.isEmpty()){
            return null;
        }

        for(Team t : getTeams(getCurrentSeasonId())){
            if(t.getPrimaryOwner().equals(ownerId)){
                return t;
            }
        }

        return null;
    }

    public Team getTeamById(int id){
        for(Team t : getTeams(getCurrentSeasonId())){
            if(t.getId() == id){
                return t;
            }
        }

        return null;
    }

    public String getPositionById(Integer i){
        String position = "";

        switch(i) {
            case 1:
                position = Position.QB.name();
                break;
            case 2:
                position = Position.RB.name();
                break;
            case 3:
                position = Position.WR.name();
                break;
            case 4:
                position = Position.TE.name();
                break;
            case 5:
                position = Position.K.name();
                break;
            case 16:
                position = Position.D.name();
                break;
        }

        return position;
    }

    public Team getTeamByAbbrev(String abbrev){
        return getTeamByAbbrev(abbrev, getCurrentSeasonId());
    }

    public Team getTeamByAbbrev(String abbrev, Integer seasonId){
        for(Team t : getTeams(seasonId)){
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
        for(Team t : getTeams(seasonId)){
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
        if(teamId == null){
            return null;
        }

        Team team = getTeam(teamId, seasonId);

        for(Member member : getSeason(seasonId).getMembers()){
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
        for(Team team : getSeason(seasonId).getTeams()){
            if(team.getId() == teamId){
                team.setSeasonId(seasonId);
                return team;
            }
        }

        return null;
    }

    private Boolean isValidWeek(Boolean includePlayoffs, ScheduleItem scheduleItem, Season season){
        return (includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")) &&
                scheduleItem.getMatchupPeriodId() < getWeek(season);
    }
}
