package ncollins.model.espn;

public class TransactionCounter {
    private float acquisitionBudgetSpent;
    private float acquisitions;
    private float drops;
    private float misc;
    private float moveToActive;
    private float moveToIR;
    private float paid;
    private float teamCharges;
    private float trades;


    // Getter Methods

    public float getAcquisitionBudgetSpent() {
        return acquisitionBudgetSpent;
    }

    public float getAcquisitions() {
        return acquisitions;
    }

    public float getDrops() {
        return drops;
    }

    public float getMisc() {
        return misc;
    }

    public float getMoveToActive() {
        return moveToActive;
    }

    public float getMoveToIR() {
        return moveToIR;
    }

    public float getPaid() {
        return paid;
    }

    public float getTeamCharges() {
        return teamCharges;
    }

    public float getTrades() {
        return trades;
    }

    // Setter Methods

    public void setAcquisitionBudgetSpent(float acquisitionBudgetSpent) {
        this.acquisitionBudgetSpent = acquisitionBudgetSpent;
    }

    public void setAcquisitions(float acquisitions) {
        this.acquisitions = acquisitions;
    }

    public void setDrops(float drops) {
        this.drops = drops;
    }

    public void setMisc(float misc) {
        this.misc = misc;
    }

    public void setMoveToActive(float moveToActive) {
        this.moveToActive = moveToActive;
    }

    public void setMoveToIR(float moveToIR) {
        this.moveToIR = moveToIR;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public void setTeamCharges(float teamCharges) {
        this.teamCharges = teamCharges;
    }

    public void setTrades(float trades) {
        this.trades = trades;
    }
}
