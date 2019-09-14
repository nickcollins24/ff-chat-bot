package ncollins.model.espn;

import java.util.List;

public class Player {
    private Boolean active;
    private Integer defaultPositionId;
    private Boolean droppable;
    private List<Integer> eligibleSlots;
    private String firstName;
    private String fullName;
    private Integer id;
    private Boolean injured;
    private String injuryStatus;
    private String jersey;
    private String lastName;
    private Integer lastNewsDate;
    private Integer lastVideoDate;
    private Integer proTeamId;
    private String seasonOutlook;
    //        private List<Stat> stats;
    private Integer universeId;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDefaultPositionId() {
        return defaultPositionId;
    }

    public void setDefaultPositionId(Integer defaultPositionId) {
        this.defaultPositionId = defaultPositionId;
    }

    public Boolean getDroppable() {
        return droppable;
    }

    public void setDroppable(Boolean droppable) {
        this.droppable = droppable;
    }

    public List<Integer> getEligibleSlots() {
        return eligibleSlots;
    }

    public void setEligibleSlots(List<Integer> eligibleSlots) {
        this.eligibleSlots = eligibleSlots;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getInjured() {
        return injured;
    }

    public void setInjured(Boolean injured) {
        this.injured = injured;
    }

    public String getInjuryStatus() {
        return injuryStatus;
    }

    public void setInjuryStatus(String injuryStatus) {
        this.injuryStatus = injuryStatus;
    }

    public String getJersey() {
        return jersey;
    }

    public void setJersey(String jersey) {
        this.jersey = jersey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getLastNewsDate() {
        return lastNewsDate;
    }

    public void setLastNewsDate(Integer lastNewsDate) {
        this.lastNewsDate = lastNewsDate;
    }

    public Integer getLastVideoDate() {
        return lastVideoDate;
    }

    public void setLastVideoDate(Integer lastVideoDate) {
        this.lastVideoDate = lastVideoDate;
    }

    public Integer getProTeamId() {
        return proTeamId;
    }

    public void setProTeamId(Integer proTeamId) {
        this.proTeamId = proTeamId;
    }

    public String getSeasonOutlook() {
        return seasonOutlook;
    }

    public void setSeasonOutlook(String seasonOutlook) {
        this.seasonOutlook = seasonOutlook;
    }

//        public List<Stat> getStats() {
//            return stats;
//        }
//
//        public void setStats(List<Stat> stats) {
//            this.stats = stats;
//        }

    public Integer getUniverseId() {
        return universeId;
    }

    public void setUniverseId(Integer universeId) {
        this.universeId = universeId;
    }
}