package ncollins.model.espn;

public class Matchup {
    private ScheduleItem scheduleItem;
    private int seasonId;

    public Matchup(ScheduleItem scheduleItem, int seasonId){
        this.setScheduleItem(scheduleItem);
        this.setSeasonId(seasonId);
    }

    public ScheduleItem getScheduleItem() {
        return scheduleItem;
    }

    public void setScheduleItem(ScheduleItem scheduleItem) {
        this.scheduleItem = scheduleItem;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Matchup)) return false;

        Matchup m = (Matchup) o;
        return m.getSeasonId() == seasonId && m.getScheduleItem().equals(scheduleItem);
    }
}
