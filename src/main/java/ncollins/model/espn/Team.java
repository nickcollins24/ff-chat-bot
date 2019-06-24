package ncollins.model.espn;

import java.util.List;

public class Team {
    private String                  abbrev;
    private int                     divisionId;
    private int                     id;
    private boolean                 isActive;
    private String                  location;
    private String                  logo;
    private String                  logoType;
    private String                  nickname;
    private List<String>            owners;
    private int                     playoffSeed;
    private Double                  points;
    private Double                  pointsAdjusted;
    private Double                  pointsDelta;
    private String                  primaryOwner;
    private int                     rankCalculatedFinal;
    private int                     rankFinal;
    private Record                  record;
    private TradeBlock              tradeBlock;
    private TransactionCounter      transactionCounter;
    private int                     waiverRank;

    // GET
    public String getAbbrev() { return abbrev; }
    public int getDivisionId() { return divisionId; }
    public int getId() { return id; }
    public boolean getIsActive() { return isActive; }
    public String getLocation() { return location; }
    public String getLogo() { return logo; }
    public String getLogoType() { return logoType; }
    public String getNickname() { return nickname; }
    public int getPlayoffSeed() { return playoffSeed; }
    public Double getPoints() { return points; }
    public Double getPointsAdjusted() { return pointsAdjusted; }
    public Double getPointsDelta() { return pointsDelta; }
    public String getPrimaryOwner() { return primaryOwner; }
    public int getRankCalculatedFinal() { return rankCalculatedFinal; }
    public int getRankFinal() { return rankFinal; }
    public Record getRecord() { return record; }
    public TradeBlock getTradeBlock() { return tradeBlock; }
    public TransactionCounter getTransactionCounter() { return transactionCounter; }
    public int getWaiverRank() { return waiverRank; }
    public List<String> getOwners() { return owners; }

    // SET
    public void setAbbrev(String abbrev) { this.abbrev = abbrev; }
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }
    public void setId(int id) { this.id = id; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public void setLocation(String location) { this.location = location; }
    public void setLogo(String logo) { this.logo = logo; }
    public void setLogoType(String logoType) { this.logoType = logoType; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPlayoffSeed(int playoffSeed) { this.playoffSeed = playoffSeed; }
    public void setPoints(Double points) { this.points = points; }
    public void setPointsAdjusted(Double pointsAdjusted) { this.pointsAdjusted = pointsAdjusted; }
    public void setPointsDelta(Double pointsDelta) { this.pointsDelta = pointsDelta; }
    public void setPrimaryOwner(String primaryOwner) { this.primaryOwner = primaryOwner; }
    public void setRankCalculatedFinal(int rankCalculatedFinal) { this.rankCalculatedFinal = rankCalculatedFinal; }
    public void setRankFinal(int rankFinal) { this.rankFinal = rankFinal; }
    public void setRecord(Record recordObject) { this.record = recordObject; }
    public void setTradeBlock(TradeBlock tradeBlockObject) { this.tradeBlock = tradeBlockObject; }
    public void setTransactionCounter(TransactionCounter transactionCounterObject) { this.transactionCounter = transactionCounterObject; }
    public void setWaiverRank(int waiverRank) { this.waiverRank = waiverRank; }
    public void setOwners(List<String> owners) { this.owners = owners; }

    private class TradeBlock {
        // Getter Methods
        // Setter Methods
    }

    private class TransactionCounter {
        private Double acquisitionBudgetSpent;
        private Double acquisitions;
        private Double drops;
        private Double misc;
        private Double moveToActive;
        private Double moveToIR;
        private Double paid;
        private Double teamCharges;
        private Double trades;

        // GET
        public Double getAcquisitionBudgetSpent() { return acquisitionBudgetSpent; }
        public Double getAcquisitions() { return acquisitions; }
        public Double getDrops() { return drops; }
        public Double getMisc() { return misc; }
        public Double getMoveToActive() { return moveToActive; }
        public Double getMoveToIR() { return moveToIR; }
        public Double getPaid() { return paid; }
        public Double getTeamCharges() { return teamCharges; }
        public Double getTrades() { return trades; }

        // SET
        public void setAcquisitionBudgetSpent(Double acquisitionBudgetSpent) { this.acquisitionBudgetSpent = acquisitionBudgetSpent; }
        public void setAcquisitions(Double acquisitions) { this.acquisitions = acquisitions; }
        public void setDrops(Double drops) { this.drops = drops; }
        public void setMisc(Double misc) { this.misc = misc; }
        public void setMoveToActive(Double moveToActive) { this.moveToActive = moveToActive; }
        public void setMoveToIR(Double moveToIR) { this.moveToIR = moveToIR; }
        public void setPaid(Double paid) { this.paid = paid; }
        public void setTeamCharges(Double teamCharges) { this.teamCharges = teamCharges; }
        public void setTrades(Double trades) { this.trades = trades; }
    }
}
