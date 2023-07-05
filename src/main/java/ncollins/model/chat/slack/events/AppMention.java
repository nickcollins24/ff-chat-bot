package ncollins.model.chat.slack.events;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *     "type": "app_mention",
 *     "user": "U061F7AUR",
 *     "text": "<@U0LAN0Z89> is it everything a river should be?",
 *     "ts": "1515449522.000016",
 *     "channel": "C123ABC456",
 *     "event_ts": "1515449522000016"
 * }
 */
public class AppMention {
    private String type;
    private String user;
    private String text;
    private String ts;
    private String channel;
    @JsonProperty("event_ts")
    private String eventTs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEventTs() {
        return eventTs;
    }

    public void setEventTs(String eventTs) {
        this.eventTs = eventTs;
    }

    @Override
    public String toString(){
        return "{" +
                    "type: " + this.type + ", " +
                    "user: " + this.user + ", " +
                    "text: " + this.text + ", " +
                    "ts: " + this.ts + ", " +
                    "channel: " + this.channel + ", " +
                    "event_ts: " + this.eventTs +
                "}";
    }
}
