package ncollins.model.espn;

public class Player {
    private Integer defaultPositionId;
    private Boolean droppable;
    private Integer[] eligibleSlots;
    private String firstName;
    private String lastName;
    private String fullName;
    private Ownership ownership;
    private Integer id;
    private Integer proTeamId;
    private Integer universeId;

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

    public Integer[] getEligibleSlots() {
        return eligibleSlots;
    }

    public void setEligibleSlots(Integer[] eligibleSlots) {
        this.eligibleSlots = eligibleSlots;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProTeamId() {
        return proTeamId;
    }

    public void setProTeamId(Integer proTeamId) {
        this.proTeamId = proTeamId;
    }

    public Integer getUniverseId() {
        return universeId;
    }

    public void setUniverseId(Integer universeId) {
        this.universeId = universeId;
    }

    private class Ownership {
        private Double percentOwned;
    }
}
