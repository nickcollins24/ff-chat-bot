package ncollins.model.espn;

import java.util.Map;

public class ScheduleItem {
    private Away    away;
    private Home    home;
    private int     id;
    private int     matchupPeriodId;
    private String  playoffTierType;
    private String  winner;

    // GET
    public Away getAway() { return away; }
    public Home getHome() { return home; }
    public int getId() { return id; }
    public int getMatchupPeriodId() { return matchupPeriodId; }
    public String getPlayoffTierType() { return playoffTierType; }
    public String getWinner() { return winner; }

    // SET
    public void setAway(Away awayObject) { this.away = awayObject; }
    public void setHome(Home homeObject) { this.home = homeObject; }
    public void setId(int id) { this.id = id; }
    public void setMatchupPeriodId(int matchupPeriodId) { this.matchupPeriodId = matchupPeriodId; }
    public void setPlayoffTierType(String playoffTierType) { this.playoffTierType = playoffTierType; }
    public void setWinner(String winner) { this.winner = winner; }

    public class Home extends Residence{
        public Home(){ super(); }
    }

    public class Away extends Residence {
        public Away(){ super(); }
    }

    public class Residence {
        private Double              adjustment;
        private Map<String,Float>   pointsByScoringPeriod;
        private int                 teamId;
        private int                 tiebreak;
        private Double              totalPoints;

        // GET
        public Double getAdjustment() { return adjustment; }
        public Map<String, Float> getPointsByScoringPeriod() { return pointsByScoringPeriod; }
        public int getTeamId() { return teamId; }
        public int getTiebreak() { return tiebreak; }
        public Double getTotalPoints() { return totalPoints; }

        // SET
        public void setAdjustment(Double adjustment) { this.adjustment = adjustment; }
        public void setPointsByScoringPeriod(Map<String, Float> pointsByScoringPeriod) { this.pointsByScoringPeriod = pointsByScoringPeriod; }
        public void setTeamId(int teamId) { this.teamId = teamId; }
        public void setTiebreak(int tiebreak) { this.tiebreak = tiebreak; }
        public void setTotalPoints(Double totalPoints) { this.totalPoints = totalPoints; }
    }
}