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
                String espnUrl = "https://fantasy.espn.com/apis/v3/games/ffl/leagueHistory/" + LEAGUE_ID + "?" +
                        "view=mBoxscore&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mTeam&seasonId=" + i;

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(espnUrl))
                        .setHeader("Content-Type", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                List<Season> season = gson.fromJson(response.body(), new TypeToken<List<Season>>(){}.getType());
                league.setSeason(season.get(0));
            } catch(Exception e){
                logger.info("no espn league data found for " + i + ", loading complete.");
                return league;
            }
        }

        return league;
    }
}
