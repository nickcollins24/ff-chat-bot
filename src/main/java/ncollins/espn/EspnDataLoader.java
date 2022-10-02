package ncollins.espn;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ncollins.model.espn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Repository
public class EspnDataLoader {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LEAGUE_YEAR = System.getenv("ESPN_LEAGUE_YEAR");
    private static final String LEAGUE_ID = System.getenv("ESPN_LEAGUE_ID");
    private static final String MEMBER_MAP = System.getenv("ESPN_MANAGER_MAP");

    private EspnHttpClient client;
    private League league;
    private Gson gson;
    private long currentSeasonLastUpdate;
    // used to map one member id to another member id
    private Map<String, String> memberMap;

    @Autowired
    public EspnDataLoader(EspnHttpClient client) {
        this.gson = new Gson();
        this.client = client;
        this.memberMap = buildMemberMapFromString(MEMBER_MAP);
        this.league = loadLeague();
    }

    /**
     * @param memberMapString of format "[memberIdFrom:memberIdTo,...]"
     * @return
     */
    private Map<String, String> buildMemberMapFromString(String memberMapString){
        Map<String, String> memberMap = new HashMap();

        for(String memberFromToString : memberMapString.split(",")){
            String[] memberIds = memberFromToString.split(":");

            memberMap.put(memberIds[0].trim(), memberIds[1].trim());
        }

        return memberMap;
    }

    public Integer getCurrentSeasonId(){
        return Integer.valueOf(LEAGUE_YEAR);
    }

    public Season getSeason(Integer seasonId){
        // refresh season data if we are retrieving the current season, and season data hasnt been refreshed for > 30 seconds
        if(seasonId.equals(getCurrentSeasonId())
                && System.currentTimeMillis() - currentSeasonLastUpdate > Duration.ofSeconds(30).toMillis()){
            this.league.setSeason(loadSeason(seasonId));
        }

        return this.league.getSeason(seasonId);
    }

    public Map<Integer,Season> getSeasons(){
        // refresh current season data if it hasnt been refreshed for > 30 seconds
        if(System.currentTimeMillis() - currentSeasonLastUpdate > Duration.ofSeconds(30).toMillis()){
            this.league.setSeason(loadSeason(getCurrentSeasonId()));
        }

        return this.league.getSeasons();
    }

    private League loadLeague(){
        logger.info("loading espn league data...");
        League league = new League();
        Season season;

        // load league data by season until we get an exception
        for(int i = getCurrentSeasonId(); i >= 0; i--) {
            season = loadSeason(i);
            if(season == null){
                return league;
            }

            league.setSeason(season);
        }

        return league;
    }

    private Season loadSeason(Integer seasonId){
        if(seasonId.equals(getCurrentSeasonId())){
            currentSeasonLastUpdate = System.currentTimeMillis();
        }

        logger.info("loading espn season data from " + seasonId + "...");

        try{
            HttpResponse<String> response = client.send(buildSeasonRequest(seasonId), HttpResponse.BodyHandlers.ofString());

            // Get Season from ESPN response if current season or not
            Season season = seasonId.equals(getCurrentSeasonId()) ?
                    gson.fromJson(response.body(), Season.class) :
                    ((List<Season>)gson.fromJson(response.body(), new TypeToken<List<Season>>(){}.getType())).get(0);

            mergeManagerData(season);

            // TODO: set trade data
            // season.setTrades(getTrades(i, null, null));

            return season;
        } catch(Exception e){
            logger.info("no espn season data found for " + seasonId + ".");
            return null;
        }
    }

    /**
     * Updates manager data to help identify users with multiple ESPN accounts.
     *
     * Note: This method has side effects.
     */
    private void mergeManagerData(Season season){
        // merge managers
        for(Member m : season.getMembers()){
            // update member id
            if(memberMap.containsKey(m.getId())){
                m.setId(memberMap.get(m.getId()));
            }
        }

        // merge managers within team
        for(Team t : season.getTeams()){
            // update primary owner's member id
            if(memberMap.containsKey(t.getPrimaryOwner())){
                t.setPrimaryOwner(memberMap.get(t.getPrimaryOwner()));
            }

            List<String> updatedOwners = new ArrayList<>();
            for(String owner : t.getOwners()){
                // update member id in owners list
                updatedOwners.add(memberMap.containsKey(owner) ? memberMap.get(owner) : owner);
            }
            t.setOwners(updatedOwners);
        }
    }

    public List<Transaction> getTransactions(Integer seasonId, Long fromDate, Long toDate, List<Integer> transactionIds){
        try{
            List<Transaction> transactions = new ArrayList();
            HttpResponse<String> response = client.send(buildTransactionsRequest(seasonId, fromDate,toDate,transactionIds), HttpResponse.BodyHandlers.ofString());
            JsonArray topics = gson.fromJson(response.body(), JsonObject.class).getAsJsonArray("topics");

            for(JsonElement t : topics){
                transactions.add(gson.fromJson(t, Transaction.class));
            }

            for(Transaction t : transactions){
                // update member ids in transaction list
                if(memberMap.containsKey(t.getAuthor())){
                    t.setAuthor(memberMap.get(t.getAuthor()));
                }
            }

            return transactions;
        } catch(Exception e){
            logger.info("Exception while retrieving transactions from espn: " + e);
            return null;
        }
    }

    public List<Trade> getTrades(Integer seasonId, Long fromDate, Long toDate){
        try{
            List<Trade> trades = new ArrayList();
            HttpResponse<String> response = client.send(buildTradesRequest(seasonId, fromDate,toDate), HttpResponse.BodyHandlers.ofString());
            JsonArray topics = gson.fromJson(response.body(), JsonObject.class).getAsJsonArray("topics");

            for(JsonElement t : topics){
                trades.add(buildTrade(gson.fromJson(t, Transaction.class)));
            }

            return trades;
        } catch(Exception e){
            logger.info("Exception while retrieving trades from espn: " + e);
            return null;
        }
    }

    private Trade buildTrade(Transaction t){
        Map<Integer, List<Player>> teamPlayersMap = new HashMap();

        for(int i=0; i < t.getMessages().length; i++){
            Integer toTeamId = t.getMessages()[i].getTo();
            Integer playerId = t.getMessages()[i].getTargetId();

            if(toTeamId > 0){
                if(teamPlayersMap.containsKey(toTeamId)){
                    teamPlayersMap.get(toTeamId).add(getPlayer(playerId));
                } else {
                    List<Player> players = new ArrayList();
                    players.add(getPlayer(playerId));
                    teamPlayersMap.put(toTeamId, players);
                }
            }
        }

        List<Integer> teamIds = new ArrayList(teamPlayersMap.keySet());
        Team team0 = getTeamById(teamIds.get(0));
        Team team1 = getTeamById(teamIds.get(1));
        List<Player> playersToTeam0 = teamPlayersMap.get(teamIds.get(0));
        List<Player> playersToTeam1 = teamPlayersMap.get(teamIds.get(1));

        return new Trade(team0, team1, playersToTeam0, playersToTeam1);
    }


    public Player getPlayer(Integer playerId){
        try{
            HttpResponse<String> response = client.send(buildPlayerRequest(playerId), HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), Player.class);
        } catch(Exception e){
            logger.info("no espn player data found for " + playerId + ".");
            return null;
        }
    }

    public Team getTeamById(int id){
        for(Team t : getTeams(getCurrentSeasonId())){
            if(t.getId() == id){
                return t;
            }
        }

        return null;
    }

    public List<Team> getTeams(Integer seasonId){
        List<Team> teams = getSeason(seasonId).getTeams();
        for(Team team : teams){
            team.setSeasonId(seasonId);
        }

        return teams;
    }

    private HttpRequest buildPlayerRequest(Integer playerId){
        String espnUrl = "https://fantasy.espn.com/apis/v3/games/ffl/seasons/"
                + getCurrentSeasonId() + "/players/" + playerId + "?view=players_wl";

        return HttpRequest.newBuilder()
                .uri(URI.create(espnUrl))
                .GET()
                .build();
    }

    private HttpRequest buildTradesRequest(Integer seasonId, Long fromDate, Long toDate){
        return buildTransactionsRequest(seasonId, fromDate, toDate, List.of(TransactionType.TRADE_ACCEPTED.getValue()));
    }

    private HttpRequest buildTransactionsRequest(Integer seasonId, Long fromDate, Long toDate, List<Integer> transactionIds){
        String espnUrl = "https://fantasy.espn.com/apis/v3/games/ffl/seasons/"
                + seasonId + "/segments/0/leagues/"
                + LEAGUE_ID + "/communication/?view=kona_league_communication";

        String dateRangeFilter = fromDate == null || toDate == null ?
                "" :
                "\"filterDateRange\":{\"value\":" + fromDate + ",\"additionalValue\":" + toDate + "},";

        String filter = "{\"topics\":" +
                "{\"filterType\":" +
                "{\"value\":[\"ACTIVITY_TRANSACTIONS\"]}," +
                "\"offset\":0," +
                "\"sortMessageDate\":{\"sortPriority\":1,\"sortAsc\":false}," +
                "\"sortFor\":{\"sortPriority\":2,\"sortAsc\":false}," +
                dateRangeFilter +
                "\"filterIncludeMessageTypeIds\":{\"value\":" + Arrays.toString(transactionIds.toArray()) + "}}}";

        return HttpRequest.newBuilder()
                .uri(URI.create(espnUrl))
                .setHeader("x-fantasy-filter", filter)
                .GET()
                .build();
    }

    private HttpRequest buildSeasonRequest(Integer seasonId){
        String espnUrl = seasonId.equals(getCurrentSeasonId()) ?
                // ESPN endpoint for current season
                "https://fantasy.espn.com/apis/v3/games/ffl/seasons/" + seasonId + "/segments/0/leagues/" + LEAGUE_ID + "?" +
                        "view=mBoxscore&view=mMatchup&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mTeam" :
                // ESPN endpoint for historical seasons
                "https://fantasy.espn.com/apis/v3/games/ffl/leagueHistory/" + LEAGUE_ID + "?" +
                        "view=mBoxscore&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mTeam&seasonId=" + seasonId;

        return HttpRequest.newBuilder()
                .uri(URI.create(espnUrl))
                .setHeader("Content-Type", "application/json")
                .GET()
                .build();
    }
}
