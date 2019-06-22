package ncollins.model.espn;

import java.util.ArrayList;

public class Team {
    private String              abbrev;
    private float               divisionId;
    private float               id;
    private boolean             isActive;
    private String              location;
    private String              logo;
    private String              logoType;
    private String              nickname;
    private ArrayList<String>   owners;
    private float               playoffSeed;
    private float               points;
    private float               pointsAdjusted;
    private float               pointsDelta;
    private String              primaryOwner;
    private float               rankCalculatedFinal;
    private float               rankFinal;
    private Record              record;
    private TradeBlock          tradeBlock;
    private TransactionCounter  transactionCounter;
    private float               waiverRank;


    // Getter Methods

    public String getAbbrev() {
        return abbrev;
    }

    public float getDivisionId() {
        return divisionId;
    }

    public float getId() {
        return id;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getLocation() {
        return location;
    }

    public String getLogo() {
        return logo;
    }

    public String getLogoType() {
        return logoType;
    }

    public String getNickname() {
        return nickname;
    }

    public float getPlayoffSeed() {
        return playoffSeed;
    }

    public float getPoints() {
        return points;
    }

    public float getPointsAdjusted() {
        return pointsAdjusted;
    }

    public float getPointsDelta() {
        return pointsDelta;
    }

    public String getPrimaryOwner() {
        return primaryOwner;
    }

    public float getRankCalculatedFinal() {
        return rankCalculatedFinal;
    }

    public float getRankFinal() {
        return rankFinal;
    }

    public Record getRecord() {
        return record;
    }

    public TradeBlock getTradeBlock() {
        return tradeBlock;
    }

    public TransactionCounter getTransactionCounter() {
        return transactionCounter;
    }

    public float getWaiverRank() {
        return waiverRank;
    }

    public ArrayList<String> getOwners() { return owners; }

    // Setter Methods

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public void setDivisionId(float divisionId) {
        this.divisionId = divisionId;
    }

    public void setId(float id) {
        this.id = id;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayoffSeed(float playoffSeed) {
        this.playoffSeed = playoffSeed;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public void setPointsAdjusted(float pointsAdjusted) {
        this.pointsAdjusted = pointsAdjusted;
    }

    public void setPointsDelta(float pointsDelta) {
        this.pointsDelta = pointsDelta;
    }

    public void setPrimaryOwner(String primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public void setRankCalculatedFinal(float rankCalculatedFinal) {
        this.rankCalculatedFinal = rankCalculatedFinal;
    }

    public void setRankFinal(float rankFinal) {
        this.rankFinal = rankFinal;
    }

    public void setRecord(Record recordObject) {
        this.record = recordObject;
    }

    public void setTradeBlock(TradeBlock tradeBlockObject) {
        this.tradeBlock = tradeBlockObject;
    }

    public void setTransactionCounter(TransactionCounter transactionCounterObject) {
        this.transactionCounter = transactionCounterObject;
    }

    public void setWaiverRank(float waiverRank) {
        this.waiverRank = waiverRank;
    }

    public void setOwners(ArrayList<String> owners) { this.owners = owners; }
}
