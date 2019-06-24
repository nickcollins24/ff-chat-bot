package ncollins.model.espn;

public class Record {
    private Away        away;
    private Division    division;
    private Home        home;
    private Overall     overall;

    // GET
    public Away getAway() { return away; }
    public Division getDivision() { return division; }
    public Home getHome() { return home; }
    public Overall getOverall() { return overall; }

    // SET
    public void setAway(Away awayObject) { this.away = awayObject; }
    public void setDivision(Division divisionObject) { this.division = divisionObject; }
    public void setHome(Home homeObject) { this.home = homeObject; }
    public void setOverall(Overall overallObject) { this.overall = overallObject; }

    private class Home {
        private Home(){ super(); }
    }

    private class Away extends Residence {
        private Away(){ super(); }
    }

    private class Division extends Residence {
        private Division(){ super(); }
    }

    private class Overall extends Residence {
        private Overall(){ super(); }
    }

    private class Residence {
        private int     gamesBack;
        private int     losses;
        private Double  percentage;
        private Double  pointsAgainst;
        private Double  pointsFor;
        private int     streakLength;
        private String  streakType;
        private int     ties;
        private int     wins;

        // GET
        public int getGamesBack() { return gamesBack; }
        public int getLosses() { return losses; }
        public Double getPercentage() { return percentage; }
        public Double getPointsAgainst() { return pointsAgainst; }
        public Double getPointsFor() { return pointsFor; }
        public int getStreakLength() { return streakLength; }
        public String getStreakType() { return streakType; }
        public int getTies() { return ties; }
        public int getWins() { return wins; }

        // SET
        public void setGamesBack(int gamesBack) { this.gamesBack = gamesBack; }
        public void setLosses(int losses) { this.losses = losses; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }
        public void setPointsAgainst(Double pointsAgainst) { this.pointsAgainst = pointsAgainst; }
        public void setPointsFor(Double pointsFor) { this.pointsFor = pointsFor; }
        public void setStreakLength(int streakLength) { this.streakLength = streakLength; }
        public void setStreakType(String streakType) { this.streakType = streakType; }
        public void setTies(int ties) { this.ties = ties; }
        public void setWins(int wins) { this.wins = wins; }
    }
}
