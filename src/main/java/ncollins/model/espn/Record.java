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

    public class Home {
        public Home(){ super(); }
    }

    public class Away extends Residence {
        public Away(){ super(); }
    }

    public class Division extends Residence {
        public Division(){ super(); }
    }

    public class Overall extends Residence{
        public Overall(){ super(); }
    }

    public class Residence {
        private Integer gamesBack = 0;
        private Integer losses = 0;
        private Double  percentage = 0.0;
        private Double  pointsAgainst = 0.0;
        private Double  pointsFor = 0.0;
        private Integer streakLength = 0;
        private String  streakType;
        private Integer ties = 0;
        private Integer wins = 0;

        // GET
        public Integer getGamesBack() { return gamesBack; }
        public Integer getLosses() { return losses; }
        public Double getPercentage() { return percentage; }
        public Double getPointsAgainst() { return pointsAgainst; }
        public Double getPointsFor() { return pointsFor; }
        public Integer getStreakLength() { return streakLength; }
        public String getStreakType() { return streakType; }
        public Integer getTies() { return ties; }
        public Integer getWins() { return wins; }

        // SET
        public void setGamesBack(Integer gamesBack) { this.gamesBack = gamesBack; }
        public void setLosses(Integer losses) { this.losses = losses; }
        public void setPercentage(Double percentage) { this.percentage = percentage; }
        public void setPointsAgainst(Double pointsAgainst) { this.pointsAgainst = pointsAgainst; }
        public void setPointsFor(Double pointsFor) { this.pointsFor = pointsFor; }
        public void setStreakLength(Integer streakLength) { this.streakLength = streakLength; }
        public void setStreakType(String streakType) { this.streakType = streakType; }
        public void setTies(Integer ties) { this.ties = ties; }
        public void setWins(Integer wins) { this.wins = wins; }

        public void addWins(Integer wins) {
            this.wins += wins;
        }

        public void addLosses(Integer losses) {
            this.losses += losses;
        }

        public void addTies(Integer ties) {
            this.ties += ties;
        }
    }
}
