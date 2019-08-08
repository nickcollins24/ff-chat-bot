package ncollins.model.espn;

import java.util.HashMap;
import java.util.Map;

public class League {
    private Map<Integer, Season> seasons;

    public League(){
        seasons = new HashMap<>();
    }

    public Map<Integer, Season> getSeasons(){
        return this.seasons;
    }

    public Season getSeason(Integer seasonId){
        return getSeasons().get(seasonId);
    }

    public void setSeason(Season season){
        seasons.put(season.getSeasonId(), season);
    }
}

