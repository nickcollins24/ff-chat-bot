package ncollins.espn;

import ncollins.model.espn.League;
import ncollins.model.espn.Season;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class EspnDataLoader {
    private HttpClient client;
    private League league;

    public EspnDataLoader() {
        this.client = HttpClient.newHttpClient();
    }

    public League loadLeague(){
        return null;
    }

    private List<Season> getSeasons(int yearFrom, int yearTo){
        List<Season> seasons = new ArrayList();

        for(int i = yearFrom; i <= yearTo; i++){
            seasons.add(getSeason(i));
        }

        return seasons;
    }

    private Season getSeason(int year){
        return null;
    }
}
