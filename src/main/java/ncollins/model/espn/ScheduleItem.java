package ncollins.model.espn;

public class ScheduleItem {
    private Away away;
    private Home home;
    private float id;
    private float matchupPeriodId;
    private String playoffTierType;
    private String winner;


    // Getter Methods

    public Away getAway() {
        return away;
    }

    public Home getHome() {
        return home;
    }

    public float getId() {
        return id;
    }

    public float getMatchupPeriodId() {
        return matchupPeriodId;
    }

    public String getPlayoffTierType() {
        return playoffTierType;
    }

    public String getWinner() {
        return winner;
    }

    // Setter Methods

    public void setAway(Away awayObject) {
        this.away = awayObject;
    }

    public void setHome(Home homeObject) {
        this.home = homeObject;
    }

    public void setId(float id) {
        this.id = id;
    }

    public void setMatchupPeriodId(float matchupPeriodId) {
        this.matchupPeriodId = matchupPeriodId;
    }

    public void setPlayoffTierType(String playoffTierType) {
        this.playoffTierType = playoffTierType;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
