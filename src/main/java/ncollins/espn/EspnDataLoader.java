package ncollins.espn;

import com.google.gson.Gson;
import ncollins.model.espn.League;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EspnDataLoader {
    private EspnHttpClient client;
    private Gson gson;
    private static final String LEAGUE_YEAR = "2019";
    private static final String LEAGUE_ID = "541969";

    public EspnDataLoader() {
        this.client = new EspnHttpClient();
        this.gson = new Gson();
    }

    public League loadLeague(){
        String espnUrl = "http://fantasy.espn.com/apis/v3/games/ffl/seasons/" + LEAGUE_YEAR + "/segments/0/leagues/" + LEAGUE_ID + "?" +
                "view=mBoxscore&view=mMatchupScore&view=mSchedule&view=mScoreboard&view=mTeam";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(espnUrl))
                .setHeader("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), League.class);
    }
}
