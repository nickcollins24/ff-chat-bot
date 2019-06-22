package ncollins.model.espn;

public class Record {
    private Away AwayObject;
    private Division DivisionObject;
    private Home HomeObject;
    private Overall OverallObject;


    // Getter Methods

    public Away getAway() {
        return AwayObject;
    }

    public Division getDivision() {
        return DivisionObject;
    }

    public Home getHome() {
        return HomeObject;
    }

    public Overall getOverall() {
        return OverallObject;
    }

    // Setter Methods

    public void setAway(Away awayObject) {
        this.AwayObject = awayObject;
    }

    public void setDivision(Division divisionObject) {
        this.DivisionObject = divisionObject;
    }

    public void setHome(Home homeObject) {
        this.HomeObject = homeObject;
    }

    public void setOverall(Overall overallObject) {
        this.OverallObject = overallObject;
    }
}
