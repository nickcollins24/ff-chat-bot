package ncollins.model.espn;

public class OwnerToOverall {
    private String ownerId;
    private String ownerName;
    private Record.Overall overall;

    public OwnerToOverall(String ownerId, String ownerName, Record.Overall overall){
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.overall = overall;
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
