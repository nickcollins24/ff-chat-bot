package ncollins.model.espn;

import java.util.List;

public class Season {
    private Status              status;
    private List<Member>        members;
    private List<ScheduleItem>  schedule;
    private int                 scoringPeriodId;
    private int                 seasonId;
    private List<Team>          teams;

    // GET
    public int getScoringPeriodId() { return scoringPeriodId; }
    public int getSeasonId() { return seasonId; }
    public Status getStatus() { return status; }
    public List<Team> getTeams() { return teams; }
    public List<Member> getMembers() { return members; }
    public List<ScheduleItem> getSchedule() { return schedule; }

    // SET
    public void setScoringPeriodId(int scoringPeriodId) { this.scoringPeriodId = scoringPeriodId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }
    public void setStatus(Status statusObject) { this.status = statusObject; }
    public void setMembers(List<Member> members) { this.members = members; }
    public void setSchedule(List<ScheduleItem> schedule) { this.schedule = schedule; }
    public void setTeams(List<Team> teams) { this.teams = teams; }
}
