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

    public class Subject {
        private Attachment[] attachments;
        @SerializedName("avatar_url")
        private String avatarUrl;
        @SerializedName("created_at")
        private Integer createdAt;
        @SerializedName("group_id")
        private String groupId;
        private String id;
        private Location location;
        private String name;
        @SerializedName("picture_url")
        private String pictureUrl;
        @SerializedName("sender_id")
        private String senderId;
        @SerializedName("sender_type")
        private String senderType;
        @SerializedName("source_guid")
        private String sourceGuid;
        private Boolean system;
        private String text;
        @SerializedName("user_id")
        private String userId;

        public Attachment[] getAttachments() {
            return attachments;
        }

        public void setAttachments(Attachment[] attachments) {
            this.attachments = attachments;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public Integer getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Integer createdAt) {
            this.createdAt = createdAt;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderType() {
            return senderType;
        }

        public void setSenderType(String senderType) {
            this.senderType = senderType;
        }

        public String getSourceGuid() {
            return sourceGuid;
        }

        public void setSourceGuid(String sourceGuid) {
            this.sourceGuid = sourceGuid;
        }

        public Boolean getSystem() {
            return system;
        }

        public void setSystem(Boolean system) {
            this.system = system;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public class Attachment {
        private int[][] charmap;
        private String placeholder;
        private String type;
    }

    public class Location {
        private String lat;
        private String lng;
        private String name;
    }
}
