package ncollins.espn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ncollins.model.espn.League;
import ncollins.model.espn.Season;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EspnDataLoader {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LEAGUE_YEAR = System.getenv("ESPN_LEAGUE_YEAR");
    private static final String LEAGUE_ID = System.getenv("ESPN_LEAGUE_ID");

    private EspnHttpClient client;
    private Gson gson;

    public EspnDataLoader() {
        this.client = new EspnHttpClient();
        this.gson = new Gson();
    }

    public League loadLeague(){
        League league = new League();

        // load league data by season until we get an exception
        for(int i = Integer.valueOf(LEAGUE_YEAR); i >= 0; i--) {
            logger.info("loading espn league data from " + i + "...");

            try{
                HttpResponse<String> response = client.send(buildSeasonRequest(i), HttpResponse.BodyHandlers.ofString());

                // Get Season from ESPN response if current season
                if(i == Integer.valueOf(LEAGUE_YEAR)){
                    Season season = gson.fromJson(response.body(), Season.class);
                    league.setSeason(season);
                // Get Season from ESPN response if historical season
                } else {
                    List<Season> season = gson.fromJson(response.body(), new TypeToken<List<Season>>(){}.getType());
                    league.setSeason(season.get(0));
                }
            } catch(Exception e){
                logger.info("no espn league data found for " + i + ", loading complete.");
                return league;
            }
        }

        return league;
    }

    private HttpRequest buildSeasonRequest(Integer seasonId){
        String espnUrl = seasonId.equals(Integer.valueOf(LEAGUE_YEAR)) ?
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
