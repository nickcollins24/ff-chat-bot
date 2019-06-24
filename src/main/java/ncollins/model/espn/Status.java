package ncollins.model.espn;

public class Status {
    private long    activatedDate;
    private int     createdAsLeagueType;
    private int     currentLeagueType;
    private int     currentMatchupPeriod;
    private int     finalScoringPeriod;
    private int     firstScoringPeriod;
    private boolean isActive;
    private boolean isExpired;
    private boolean isFull;
    private boolean isPlayoffMatchupEdited;
    private boolean isToBeDeleted;
    private boolean isViewable;
    private boolean isWaiverOrderEdited;
    private int     latestScoringPeriod;
    private long    standingsUpdateDate;
    private int     teamsJoined;
    private int     transactionScoringPeriod;
    private long    waiverLastExecutionDate;

    // GET
    public long getActivatedDate() { return activatedDate; }
    public int getCreatedAsLeagueType() { return createdAsLeagueType; }
    public int getCurrentLeagueType() { return currentLeagueType; }
    public int getCurrentMatchupPeriod() { return currentMatchupPeriod; }
    public int getFinalScoringPeriod() { return finalScoringPeriod; }
    public int getFirstScoringPeriod() { return firstScoringPeriod; }
    public boolean getIsActive() { return isActive; }
    public boolean getIsExpired() { return isExpired; }
    public boolean getIsFull() { return isFull; }
    public boolean getIsPlayoffMatchupEdited() { return isPlayoffMatchupEdited; }
    public boolean getIsToBeDeleted() { return isToBeDeleted; }
    public boolean getIsViewable() { return isViewable; }
    public boolean getIsWaiverOrderEdited() { return isWaiverOrderEdited; }
    public int getLatestScoringPeriod() { return latestScoringPeriod; }
    public long getStandingsUpdateDate() { return standingsUpdateDate; }
    public int getTeamsJoined() { return teamsJoined; }
    public int getTransactionScoringPeriod() { return transactionScoringPeriod; }
    public long getWaiverLastExecutionDate() { return waiverLastExecutionDate; }

    // SET
    public void setActivatedDate(long activatedDate) { this.activatedDate = activatedDate; }
    public void setCreatedAsLeagueType(int createdAsLeagueType) { this.createdAsLeagueType = createdAsLeagueType; }
    public void setCurrentLeagueType(int currentLeagueType) { this.currentLeagueType = currentLeagueType; }
    public void setCurrentMatchupPeriod(int currentMatchupPeriod) { this.currentMatchupPeriod = currentMatchupPeriod; }
    public void setFinalScoringPeriod(int finalScoringPeriod) { this.finalScoringPeriod = finalScoringPeriod; }
    public void setFirstScoringPeriod(int firstScoringPeriod) { this.firstScoringPeriod = firstScoringPeriod; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public void setIsExpired(boolean isExpired) { this.isExpired = isExpired; }
    public void setIsFull(boolean isFull) { this.isFull = isFull; }
    public void setIsPlayoffMatchupEdited(boolean isPlayoffMatchupEdited) { this.isPlayoffMatchupEdited = isPlayoffMatchupEdited; }
    public void setIsToBeDeleted(boolean isToBeDeleted) { this.isToBeDeleted = isToBeDeleted; }
    public void setIsViewable(boolean isViewable) { this.isViewable = isViewable; }
    public void setIsWaiverOrderEdited(boolean isWaiverOrderEdited) { this.isWaiverOrderEdited = isWaiverOrderEdited; }
    public void setLatestScoringPeriod(int latestScoringPeriod) { this.latestScoringPeriod = latestScoringPeriod; }
    public void setStandingsUpdateDate(long standingsUpdateDate) { this.standingsUpdateDate = standingsUpdateDate; }
    public void setTeamsJoined(int teamsJoined) { this.teamsJoined = teamsJoined; }
    public void setTransactionScoringPeriod(int transactionScoringPeriod) { this.transactionScoringPeriod = transactionScoringPeriod; }
    public void setWaiverLastExecutionDate(long waiverLastExecutionDate) { this.waiverLastExecutionDate = waiverLastExecutionDate; }
}
