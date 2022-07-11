package ncollins.model.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subject {
    private Attachment[] attachments;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("created_at")
    private Integer createdAt;
    @JsonProperty("group_id")
    private String groupId;
    private String id;
    private String name;
    @JsonProperty("sender_id")
    private String senderId;
    @JsonProperty("sender_type")
    private String senderType;
    @JsonProperty("source_guid")
    private String sourceGuid;
    private Boolean system;
    private String text;
    @JsonProperty("user_id")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String printAttachments(Attachment[] attachments){
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for(Attachment a : attachments){
            sb.append(a.toString());
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public String toString(){
        return "{" +
                    "attachments: " + printAttachments(this.attachments) + ", " +
                    "avatarUrl: " + this.avatarUrl + ", " +
                    "createdAt: " + this.createdAt + ", " +
                    "groupId: " + this.groupId + ", " +
                    "id: " + this.id + ", " +
                    "name: " + this.name + ", " +
                    "senderId: " + this.senderId + ", " +
                    "senderType: " + this.senderType + ", " +
                    "sourceGuid: " + this.sourceGuid + ", " +
                    "system: " + this.system + ", " +
                    "text: " + this.text + ", " +
                    "userId: " + this.userId +
               "}";
    }
}
