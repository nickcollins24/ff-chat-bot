package ncollins.espn;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ncollins.model.espn.League;
import ncollins.model.espn.Player;
import ncollins.model.espn.Season;
import ncollins.model.espn.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EspnDataLoader {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LEAGUE_YEAR = System.getenv("ESPN_LEAGUE_YEAR");
    private static final String LEAGUE_ID = System.getenv("ESPN_LEAGUE_ID");

    private EspnHttpClient client;
    private League league;
    private Gson gson;
    private long currentSeasonLastUpdate;

    public EspnDataLoader() {
        this.gson = new Gson();
        this.client = new EspnHttpClient();
        this.league = loadLeague();
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
            return seasonId.equals(getCurrentSeasonId()) ?
                    gson.fromJson(response.body(), Season.class) :
                    ((List<Season>)gson.fromJson(response.body(), new TypeToken<List<Season>>(){}.getType())).get(0);
        } catch(Exception e){
            logger.info("no espn season data found for " + seasonId + ".");
            return null;
        }
    }

    public List<Transaction> getTransactions(long fromDate, long toDate, List<Integer> transactionIds){
        try{
            List<Transaction> transactions = new ArrayList();
            HttpResponse<String> response = client.send(buildTransactionsRequest(fromDate,toDate,transactionIds), HttpResponse.BodyHandlers.ofString());
            JsonArray topics = gson.fromJson(response.body(), JsonObject.class).getAsJsonArray("topics");

            for(JsonElement t : topics){
                transactions.add(gson.fromJson(t, Transaction.class));
            }

            return transactions;
        } catch(Exception e){
            logger.info("Exception while retrieving transactions from espn: " + e);
            return null;
        }
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

    private HttpRequest buildPlayerRequest(Integer playerId){
        String espnUrl = "https://fantasy.espn.com/apis/v3/games/ffl/seasons/"
                + getCurrentSeasonId() + "/players/" + playerId + "?view=players_wl";

        return HttpRequest.newBuilder()
                .uri(URI.create(espnUrl))
                .GET()
                .build();
    }

    private HttpRequest buildTransactionsRequest(long fromDate, long toDate, List<Integer> transactionIds){
        String espnUrl = "https://fantasy.espn.com/apis/v3/games/ffl/seasons/"
                + getCurrentSeasonId() + "/segments/0/leagues/"
                + LEAGUE_ID + "/communication/?view=kona_league_communication";

        String filter = "{\"topics\":" +
                "{\"filterType\":" +
                "{\"value\":[\"ACTIVITY_TRANSACTIONS\"]}," +
                "\"offset\":0," +
                "\"sortMessageDate\":{\"sortPriority\":1,\"sortAsc\":false}," +
                "\"sortFor\":{\"sortPriority\":2,\"sortAsc\":false}," +
                "\"filterDateRange\":{\"value\":" + fromDate + ",\"additionalValue\":" + toDate + "}," +
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
                        "view=mBoxscore&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mTeam" :
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
