package ncollins.model.espn;

public class Score {
    private int     teamId;
    private int     teamIdAgainst;
    private Double  points;
    private Double  pointsAgainst;
    private Outcome outcome;
    private int     matchupPeriodId;

    public Score(ScheduleItem.Residence teamFor, ScheduleItem.Residence teamAgainst, int matchupPeriodId){
        this.teamId = teamFor.getTeamId();
        this.teamIdAgainst = teamAgainst.getTeamId();
        this.points = teamFor.getTotalPoints();
        this.pointsAgainst = teamAgainst.getTotalPoints();
        this.matchupPeriodId = matchupPeriodId;

        if(points > pointsAgainst){
            this.outcome = Outcome.WIN;
        } else if(points < pointsAgainst){
            this.outcome = Outcome.LOSS;
        } else {
            this.outcome = Outcome.TIE;
        }
    }

    // GET
    public int getTeamId() { return teamId; }
    public int getTeamIdAgainst() { return teamIdAgainst; }
    public Double getPointsAgainst() { return pointsAgainst; }
    public Outcome getOutcome() { return outcome; }
    public int getMatchupPeriodId() { return matchupPeriodId; }
    public Double getPoints() { return points; }

    // SET
    public void setTeamId(int teamId) { this.teamId = teamId; }
    public void setTeamIdAgainst(int teamIdAgainst) { this.teamIdAgainst = teamIdAgainst; }
    public void setPoints(Double points) { this.points = points; }
    public void setPointsAgainst(Double pointsAgainst) { this.pointsAgainst = pointsAgainst; }
    public void setOutcome(Outcome outcome) { this.outcome = outcome; }
    public void setMatchupPeriodId(int matchupPeriodId) { this.matchupPeriodId = matchupPeriodId; }
}
