package ncollins.model.chat.slack.events;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * 		"client_msg_id": "ad3f6428-82cd-4755-9810-8d7b5acaf99a",
 * 		"type": "message",
 * 		"text": "Hulloooo Jan :wave:",
 * 		"user": "U05E1T188V9",
 * 		"ts": "1688007583.410659",
 * 		"blocks": [{
 * 			"type": "rich_text",
 * 			"block_id": "hbw5L",
 * 			"elements": [{
 * 				"type": "rich_text_section",
 * 				"elements": [{
 * 					"type": "text",
 * 					"text": "Hulloooo Jan "
 *                                }, {
 * 					"type": "emoji",
 * 					"name": "wave",
 * 					"unicode": "1f44b"
 *                }]* 			}        ]
 * 		}],
 * 		"team": "T05DMEU62AK",
 * 		"thread_ts": "1688007380.513629",
 * 		"parent_user_id": "U05E1T188V9",
 * 		"channel": "C05DMEU6FF1",
 * 		"event_ts": "1688007583.410659",
 * 		"channel_type": "channel"
 */
public class Message {
    @JsonProperty("client_msg_id")
    private String clientMsgId;
    private String type;
    private String text;
    private String user;
    private String ts;
    private String team;
    @JsonProperty("thread_ts")
    private String threadTs = "";
    @JsonProperty("parent_user_id")
    private String parentUserId;
    private String channel;
    @JsonProperty("event_ts")
    private String eventTs;
    @JsonProperty("channel_type")
    private String channelType;

    public String getClientMsgId() {
        return clientMsgId;
    }

    public void setClientMsgId(String clientMsgId) {
        this.clientMsgId = clientMsgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getThreadTs() {
        return threadTs;
    }

    public void setThreadTs(String threadTs) {
        this.threadTs = threadTs;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    @Override
    public String toString(){
        return "{" +
                    "client_msg_id: " + this.clientMsgId + ", " +
                    "type: " + this.type + ", " +
                    "text: " + this.text + ", " +
                    "user: " + this.user + ", " +
                    "ts: " + this.ts + ", " +
                    "team: " + this.team + ", " +
                    "thread_ts: " + this.threadTs + ", " +
                    "parent_user_id: " + this.parentUserId + ", " +
                    "channel: " + this.channel + ", " +
                    "event_ts: " + this.eventTs + ", " +
                    "channel_type: " + this.channelType +
                "}";
    }
}
