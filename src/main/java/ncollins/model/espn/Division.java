package ncollins.model.espn;

public class Division {
    private float gamesBack;
    private float losses;
    private float percentage;
    private float pointsAgainst;
    private float pointsFor;
    private float streakLength;
    private String streakType;
    private float ties;
    private float wins;


    // Getter Methods

    public float getGamesBack() {
        return gamesBack;
    }

    public float getLosses() {
        return losses;
    }

    public float getPercentage() {
        return percentage;
    }

    public float getPointsAgainst() {
        return pointsAgainst;
    }

    public float getPointsFor() {
        return pointsFor;
    }

    public float getStreakLength() {
        return streakLength;
    }

    public String getStreakType() {
        return streakType;
    }

    public float getTies() {
        return ties;
    }

    public float getWins() {
        return wins;
    }

    // Setter Methods

    public void setGamesBack(float gamesBack) {
        this.gamesBack = gamesBack;
    }

    public void setLosses(float losses) {
        this.losses = losses;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void setPointsAgainst(float pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    public void setPointsFor(float pointsFor) {
        this.pointsFor = pointsFor;
    }

    public void setStreakLength(float streakLength) {
        this.streakLength = streakLength;
    }

    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }

    public void setTies(float ties) {
        this.ties = ties;
    }

    public void setWins(float wins) {
        this.wins = wins;
    }
}
