package ncollins.model.chat;


import com.google.gson.annotations.SerializedName;

/***
 * Sample response:
 *
 * [{
 * "channel":"/user/...",
 * "clientId":"...",
 * "id":"...",
 * "data":{
 *      "alert":"...: ...",
 *      "received_at":1560109645000,
 *      "subject":{
 *          "attachments":[{
 * 				"charmap": [
 * 					[19, 13]
 * 				],
 * 				"placeholder": "�",
 * 				"type": "emoji"
 *          }],
 *          "avatar_url":"...",
 *          "created_at":1560109645,
 *          "group_id":"...",
 *          "id":"...",
 *          "location":{"lat":"","lng":"","name":null},
 *          "name":"...",
 *          "picture_url":null,
 *          "sender_id":"...",
 *          "sender_type":"user",
 *          "source_guid":"...",
 *          "system":false,
 *          "text":"test text",
 *          "user_id":"..."},
 *      "type":"line.create"}}]
 */
public class ChatResponse {
    private String channel;
    private String clientId;
    private String id;
    private Data data;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String alert;
        @SerializedName("received_at")
        private Integer receivedAt;
        private Subject subject;
        private String type;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public Integer getReceivedAt() {
            return receivedAt;
        }

        public void setReceivedAt(Integer receivedAt) {
            this.receivedAt = receivedAt;
        }

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
