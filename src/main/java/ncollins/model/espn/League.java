package ncollins.model.espn;

import java.util.ArrayList;

public class League {
    private DraftDetail         draftDetail;
    private Status              status;
    private ArrayList<Member>   members;
    private ArrayList<ScheduleItem>   schedule;
    private int                 scoringPeriodId;
    private int                 seasonId;
    private ArrayList<Team>   teams;

    // GET
    public DraftDetail getDraftDetail() {
        return draftDetail;
    }
    public int getScoringPeriodId() { return scoringPeriodId; }
    public int getSeasonId() { return seasonId; }
    public Status getStatus() { return status; }
    public ArrayList<Team> getTeams() { return teams; }
    public ArrayList<Member> getMembers() { return members; }
    public ArrayList<ScheduleItem> getSchedule() { return schedule; }

    // SET
    public void setDraftDetail(DraftDetail draftDetail) { this.draftDetail = draftDetail; }
    public void setScoringPeriodId(int scoringPeriodId) { this.scoringPeriodId = scoringPeriodId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }
    public void setStatus(Status statusObject) { this.status = statusObject; }
    public void setMembers(ArrayList<Member> members) { this.members = members; }
    public void setSchedule(ArrayList<ScheduleItem> schedule) { this.schedule = schedule; }
    public void setTeams(ArrayList<Team> teams) { this.teams = teams; }
}

