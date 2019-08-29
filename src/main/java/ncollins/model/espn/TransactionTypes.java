package ncollins.model.espn;

import java.util.HashMap;
import java.util.Map;

public final class TransactionTypes {
    private Map<Integer,String> types = new HashMap();

    public TransactionTypes(){
        // Add
        types.put(178, "{toTeamName} added {playerName}, {proTeamAbbrev} {positionAbbrev} from Free Agency to {placement}");
        types.put(180, "{toTeamName} added {playerName}, {proTeamAbbrev} {positionAbbrev} from Waivers to {placement}");

        // Drop
        types.put(179, "{toTeamName} dropped {playerName}, {proTeamAbbrev} {positionAbbrev} from Free Agency to {placement}");
        types.put(181, "{toTeamName} dropped {playerName}, {proTeamAbbrev} {positionAbbrev} from Waivers to {placement}");
        types.put(239, "{toTeamName} dropped {playerName}, {proTeamAbbrev} {positionAbbrev} from roster");

        // Trade
        types.put(182, "{teamName} Accepted Trade {userName}");
        types.put(183, "Declined Trade");
        types.put(184, "{userName} Proposed Trade");
        types.put(185, "Trade Upheld");
        types.put(186, "Trade Vetoed");
        types.put(187, "Trade Processed");
        types.put(190, "Proposed Trade");
    }

    public String get(Integer id){
        return types.get(id);
    }
}