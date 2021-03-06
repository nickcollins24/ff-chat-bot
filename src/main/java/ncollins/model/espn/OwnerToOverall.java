package ncollins.model.espn;

public class OwnerToOverall {
    private String ownerId;
    private String ownerName;
    private Integer seasonId;
    private Record.Overall overall;

    public OwnerToOverall(String ownerId, String ownerName, Record.Overall overall){
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.overall = overall;
    }

    public OwnerToOverall(String ownerId, String ownerName, Record.Overall overall, Integer seasonId){
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.overall = overall;
        this.seasonId = seasonId;
    }

    public String getOwnerId(){
        return this.ownerId;
    }

    public String getOwnerName(){
        return this.ownerName;
    }

    public Record.Overall getOverall(){
        return this.overall;
    }
}
