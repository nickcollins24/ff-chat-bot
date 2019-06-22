package ncollins.model.espn;

public class DraftDetail {
    private boolean drafted;
    private boolean inProgress;

    // GET
    public boolean getDrafted() {
        return drafted;
    }
    public boolean getInProgress() {
        return inProgress;
    }

    // SET
    public void setDrafted(boolean drafted) {
        this.drafted = drafted;
    }
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
