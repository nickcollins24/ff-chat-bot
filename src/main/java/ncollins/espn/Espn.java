package ncollins.espn;

import ncollins.espn.comparators.*;
import ncollins.model.Order;
import ncollins.model.espn.*;
import ncollins.model.espn.Record;

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

    public List<RosterForCurrentScoringPeriod.RosterEntry> getPlayersByWeeklyPF(Order order, int total, int week, Position position){
        List<ScheduleItem> scheduleItems = loader.getSeason(getCurrentSeasonId()).getSchedule();
        List<RosterForCurrentScoringPeriod.RosterEntry> players = new ArrayList();

        for(ScheduleItem item : scheduleItems){
            if(item.getMatchupPeriodId().equals(week)){
                for(RosterForCurrentScoringPeriod.RosterEntry e : item.getHome().getRosterForCurrentScoringPeriod().getRosterEntries()){
                    if(e.getPlayerPoolEntry().getRosterLocked() &&
                            (position == null || isMatchingPosition(e.getPlayerPoolEntry().getPlayer().getDefaultPositionId(), position))){
                        players.add(e);
                    }
                }
                for(RosterForCurrentScoringPeriod.RosterEntry e : item.getAway().getRosterForCurrentScoringPeriod().getRosterEntries()){
                    if(e.getPlayerPoolEntry().getRosterLocked() &&
                            (position == null || isMatchingPosition(e.getPlayerPoolEntry().getPlayer().getDefaultPositionId(), position))){
                        players.add(e);
                    }
                }
            }
        }

        players.sort(new SortRosterEntriesByPoints(order));
        return players.subList(0, Math.min(players.size(), total));
    }

    /***
     *  a Juju describes a week in which a fantasy football team:
     *          1) has a bottom-five score for the week
     *          2) is below the average score for that week
     *          3) score is less than 100
     *          4) wins
     */
    public List<Score> getJujus(Integer seasonId){
        List<Score> jujus = new ArrayList();

        for(int i=1; i < getCurrentScoringPeriodId(); i++){
            jujus.addAll(getJujus(i, seasonId));
        }

        return jujus;
    }

    /***
     *  a Juju describes a week in which a fantasy football team:
     *          1) has a bottom-five score for the week
     *          2) is below the average score for that week
     *          3) score is less than 100
     *          4) wins
     */
    public List<Score> getJujus(Integer scoringPeriodId, Integer seasonId){
        List<Score> jujus = new ArrayList();
        List<Score> scores = getScoresSorted(Order.ASC, scoringPeriodId, seasonId, true);
        Double totalPoints = 0.0;

        for(Score score : scores){
            totalPoints += score.getPoints();
        }

        Double averagePoints = totalPoints/(scores.size());
        for(Score score : scores.subList(0, Math.min(scores.size(), scores.size()/2))){
            if(score.getOutcome().equals(Outcome.WIN)
            && score.getPoints() < averagePoints
            && score.getPoints() < 100){
                jujus.add(score);
            }
        }

        return jujus;
    }

    /***
     *  a Salty describes a week in which a fantasy football team:
     *          1) has a top-five score for the week
     *          2) is above the average score for the week
     *          3) score is greater than or equal to 100
     *          4) loses
     */
    public List<Score> getSalties(Integer seasonId){
        List<Score> salties = new ArrayList();

        for(int i=1; i < getCurrentScoringPeriodId(); i++){
            salties.addAll(getSalties(i, seasonId));
        }

        return salties;
    }

    /***
     *  a Salty describes a week in which a fantasy football team:
     *          1) has a top-five score for the week
     *          2) is above the average score for the week
     *          3) score is greater than or equal to 100
     *          4) loses
     */
    public List<Score> getSalties(Integer scoringPeriodId, Integer seasonId){
        List<Score> salties = new ArrayList();
        List<Score> scores = getScoresSorted(Order.DESC, scoringPeriodId, seasonId, true);
        Double totalPoints = 0.0;

        for(Score score : scores){
            totalPoints += score.getPoints();
        }

        Double averagePoints = totalPoints/(scores.size());
        for(Score score : scores.subList(0, Math.min(scores.size(), scores.size()/2))){
            if(score.getOutcome().equals(Outcome.LOSS)
                    && score.getPoints() > averagePoints
                    && score.getPoints() >= 100){
                salties.add(score);
            }
        }

        return salties;
    }

    public List<Score> getScoresSorted(Order order, Integer scoringPeriodId, Integer seasonId, boolean includePlayoffs){
        return getScoresSorted(order, null, scoringPeriodId, seasonId, includePlayoffs);
    }

    public List<Score> getScoresSorted(Order order, Integer total, Integer scoringPeriodId, Integer seasonId, boolean includePlayoffs){
        List<Score> scores = seasonId != null ?
                getScores(scoringPeriodId, seasonId, includePlayoffs) :
                getScoresAllTime(scoringPeriodId, includePlayoffs);

        scores.sort(new SortScoresByPoints(order));

        return total == null ?
                scores :
                scores.subList(0, Math.min(scores.size(), total));
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

    public List<Matchup> getMatchups(Integer scoringPeriodId, Integer seasonId){
        List<Matchup> matchups = new ArrayList();

        Season season = getSeason(seasonId);
        for(ScheduleItem scheduleItem : season.getSchedule()) {
            if (scoringPeriodId.equals(scheduleItem.getMatchupPeriodId())) {
                matchups.add(new Matchup(scheduleItem, seasonId));
            }
        }

        return matchups;
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

    public Map<Member, Record> getRecordBetween(Member m0, Member m1){
        List<Matchup> matchups = getMatchupsBetween(m0, m1);
        Map<Member, Record> memberMap = new HashMap();
        Record r0 = new Record();
        r0.setOverall(new Record().new Overall());

        Record r1 = new Record();
        r1.setOverall(new Record().new Overall());

        memberMap.put(m0, r0);
        memberMap.put(m1, r1);

        for (Matchup m : matchups) {
            Member homeMember = getMemberByTeamId(m.getScheduleItem().getHome().getTeamId(), m.getSeasonId());
            Member awayMember = getMemberByTeamId(m.getScheduleItem().getAway().getTeamId(), m.getSeasonId());

            // home team won
            if (m.getScheduleItem().getHome().getTotalPoints() > m.getScheduleItem().getAway().getTotalPoints()) {
                memberMap.get(homeMember).getOverall().addWins(1);
                memberMap.get(awayMember).getOverall().addLosses(1);
            // away team won
            } else if (m.getScheduleItem().getAway().getTotalPoints() > m.getScheduleItem().getHome().getTotalPoints()) {
                memberMap.get(homeMember).getOverall().addLosses(1);
                memberMap.get(awayMember).getOverall().addWins(1);
            // tie
            } else {
                memberMap.get(homeMember).getOverall().addTies(1);
                memberMap.get(awayMember).getOverall().addTies(1);
            }
        }

        return memberMap;
    }

    public List<Team> getSackos(){
        List<Team> sackos = new ArrayList();
        Map<Integer, Season> seasonMap = getSeasons();

        //iterate seasons
        for(Season season : seasonMap.values()){
            //only iterate completed seasons
            if(season.getSeasonId() != getCurrentSeasonId()) {
                Team sacko = null;

                //find lowest ranked team for each season
                for (Team team : season.getTeams()) {
                    if (sacko == null || team.getRankCalculatedFinal() > sacko.getRankCalculatedFinal()) {
                        sacko = team;
                    }
                }

                sacko.setSeasonId(season.getSeasonId());
                sackos.add(sacko);
            }
        }


        sackos.sort(new SortTeamsBySeasonId(Order.ASC));
        return sackos;

    }

    public List<Team> getChamps(){
        List<Team> champs = new ArrayList();
        Map<Integer, Season> seasonMap = getSeasons();

        //iterate seasons
        for(Season season : seasonMap.values()){
            //only iterate completed seasons
            if(season.getSeasonId() != getCurrentSeasonId()) {
                Team champ = null;

                //find lowest ranked team for each season
                for (Team team : season.getTeams()) {
                    if (champ == null || team.getRankCalculatedFinal() < champ.getRankCalculatedFinal()) {
                        champ = team;
                    }
                }

                champ.setSeasonId(season.getSeasonId());
                champs.add(champ);
            }
        }


        champs.sort(new SortTeamsBySeasonId(Order.ASC));
        return champs;

    }

//    public List<OwnerToOverall> getRecordThroughWeek(Order order, int total, int week){
//        List<Matchup> matchups = getMatchupsAllTime(true, true);
//
//        for(Matchup m : matchups){
//            if(m.getScheduleItem().getMatchupPeriodId() <= week){
//                m.
//            }
//        }
//        return null;
//    }

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

    public Boolean isMatchingPosition(Integer positionId, Position position){
        return positionId.equals(position.getValue()) ||
                (position.equals(Position.FLEX) &&
                (positionId.equals(Position.RB.getValue()) ||
                 positionId.equals(Position.WR.getValue()) ||
                 positionId.equals(Position.TE.getValue())));
    }

    private Boolean isValidWeek(Boolean includePlayoffs, ScheduleItem scheduleItem, Season season){
        return (includePlayoffs || scheduleItem.getPlayoffTierType().equals("NONE")) &&
                scheduleItem.getMatchupPeriodId() < getWeek(season);
    }
}
