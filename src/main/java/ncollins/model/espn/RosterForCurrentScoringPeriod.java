package ncollins.model.espn;

import java.util.List;

public class RosterForCurrentScoringPeriod {
    private List<RosterEntry> entries;

    public List<RosterEntry> getRosterEntries() { return entries; }
    public void setRosterEntries(List<RosterEntry> entries) { this.entries = entries; }

    public class RosterEntry {
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

    public class PlayerPoolEntry {
        private Double appliedStatTotal;
        private Integer id;
        private Integer keeperValue;
        private Integer keeperValueFuture;
        private Boolean lineupLocked;
        private Integer onTeamId;
        private Player player;
        private boolean rosterLocked;
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

        public boolean getRosterLocked() {
            return rosterLocked;
        }

        public void setRosterLocked(boolean rosterLocked) {
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
}
