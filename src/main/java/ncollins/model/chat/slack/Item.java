package ncollins.model.chat.slack;

public class Item {
    private String type;
    private String channel;
    private String ts;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String toString(){
        return "{" +
                "type: " + this.type + ", " +
                "ts: " + this.ts + ", " +
                "channel: " + this.channel +
        "}";
    }
}
