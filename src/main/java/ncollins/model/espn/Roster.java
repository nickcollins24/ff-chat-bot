package ncollins.model.espn;

import java.util.List;

public class Roster {
    private List<RosterEntry> entries;

    public List<RosterEntry> getRosterEntries() { return entries; }
    public void setRosterEntries(List<RosterEntry> entries) { this.entries = entries; }

    private class RosterEntry {
        private Integer acquisitionDate;
        private String acquisitionType;
        private String injuryStatus;
        private Integer lineupSlotId;
        private Integer playerId;
        private PlayerPoolEntry playerPoolEntry;
        private String status;

        public Integer getAcquisitionDate() {
            return acquisitionDate;
        }

        public void setAcquisitionDate(Integer acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
        }

        public String getAcquisitionType() {
            return acquisitionType;
        }

        public void setAcquisitionType(String acquisitionType) {
            this.acquisitionType = acquisitionType;
        }

        public String getInjuryStatus() {
            return injuryStatus;
        }

        public void setInjuryStatus(String injuryStatus) {
            this.injuryStatus = injuryStatus;
        }

        public Integer getLineupSlotId() {
            return lineupSlotId;
        }

        public void setLineupSlotId(Integer lineupSlotId) {
            this.lineupSlotId = lineupSlotId;
        }

        public Integer getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Integer playerId) {
            this.playerId = playerId;
        }

        public PlayerPoolEntry getPlayerPoolEntry() {
            return playerPoolEntry;
        }

        public void setPlayerPoolEntry(PlayerPoolEntry playerPoolEntry) {
            this.playerPoolEntry = playerPoolEntry;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    private class PlayerPoolEntry {
        private Double appliedStatTotal;
        private Integer id;
        private Integer keeperValue;
        private Integer keeperValueFuture;
        private Boolean lineupLocked;
        private Integer onTeamId;
        private Player player;
        private Boolean rosterLocked;
        private String status;
        private Boolean tradeLocked;

        public Double getAppliedStatTotal() {
            return appliedStatTotal;
        }

        public void setAppliedStatTotal(Double appliedStatTotal) {
            this.appliedStatTotal = appliedStatTotal;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getKeeperValue() {
            return keeperValue;
        }

        public void setKeeperValue(Integer keeperValue) {
            this.keeperValue = keeperValue;
        }

        public Integer getKeeperValueFuture() {
            return keeperValueFuture;
        }

        public void setKeeperValueFuture(Integer keeperValueFuture) {
            this.keeperValueFuture = keeperValueFuture;
        }

        public Boolean getLineupLocked() {
            return lineupLocked;
        }

        public void setLineupLocked(Boolean lineupLocked) {
            this.lineupLocked = lineupLocked;
        }

        public Integer getOnTeamId() {
            return onTeamId;
        }

        public void setOnTeamId(Integer onTeamId) {
            this.onTeamId = onTeamId;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Boolean getRosterLocked() {
            return rosterLocked;
        }

        public void setRosterLocked(Boolean rosterLocked) {
            this.rosterLocked = rosterLocked;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Boolean getTradeLocked() {
            return tradeLocked;
        }

        public void setTradeLocked(Boolean tradeLocked) {
            this.tradeLocked = tradeLocked;
        }
    }

    private class Player {
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
}
