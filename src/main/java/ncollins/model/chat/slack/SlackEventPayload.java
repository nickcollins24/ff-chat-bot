package ncollins.model.chat.slack;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *     "type": "event_callback",
 *     "token": "XXYYZZ",
 *     "team_id": "T123ABC456",
 *     "api_app_id": "A123ABC456",
 *     "event": {
 *             "type": "name_of_event",
 *             "event_ts": "1234567890.123456",
 *             "user": "U123ABC456",
 *             ...
 *     },
 *     "event_context": "EC123ABC456",
 *     "event_id": "Ev123ABC456",
 *     "event_time": 1234567890,
 *     "authorizations": [
 *         {
 *             "enterprise_id": "E123ABC456",
 *             "team_id": "T123ABC456",
 *             "user_id": "U123ABC456",
 *             "is_bot": false,
 *             "is_enterprise_install": false,
 *         }
 *     ],
 *     "is_ext_shared_channel": false,
 *     "context_team_id": "T123ABC456",
 *     "context_enterprise_id": null
 * }
 */
public class SlackEventPayload {
    private String challenge; // used only for slack webhook validation
    private String type;
    private String token;
    @JsonProperty("team_id")
    private String teamId;
    @JsonProperty("api_app_id")
    private String apiAppId;
    private Event event;
    @JsonProperty("event_context")
    private String eventContext;
    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("event_time")
    private int eventTime;
    private Authorization[] authorizations;
    @JsonProperty("is_ext_shared_channel")
    private boolean isExtSharedChannel;
    @JsonProperty("context_team_id")
    private String contextTeamId;
    @JsonProperty("context_enterprise_id")
    private String contextEnterpriseId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getApiAppId() {
        return apiAppId;
    }

    public void setApiAppId(String apiAppId) {
        this.apiAppId = apiAppId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getEventContext() {
        return eventContext;
    }

    public void setEventContext(String eventContext) {
        this.eventContext = eventContext;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public Authorization[] getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Authorization[] authorizations) {
        this.authorizations = authorizations;
    }

    public boolean isExtSharedChannel() {
        return isExtSharedChannel;
    }

    public void setExtSharedChannel(boolean extSharedChannel) {
        isExtSharedChannel = extSharedChannel;
    }

    public String getContextTeamId() {
        return contextTeamId;
    }

    public void setContextTeamId(String contextTeamId) {
        this.contextTeamId = contextTeamId;
    }

    public String getContextEnterpriseId() {
        return contextEnterpriseId;
    }

    public void setContextEnterpriseId(String contextEnterpriseId) {
        this.contextEnterpriseId = contextEnterpriseId;
    }

    @Override
    public String toString(){
        return "{" +
                    "challenge: " + this.challenge + ", " +
                    "type: " + this.type + ", " +
                    "token: " + this.token + ", " +
                    "team_id: " + this.teamId + ", " +
                    "api_app_id: " + this.apiAppId + ", " +
                    "event: " + this.event + ", " +
                    "event_context: " + this.eventContext + ", " +
                    "event_id: " + this.eventId + ", " +
                    "event_time: " + this.eventTime + ", " +
                    "authorizations: " + this.authorizations + ", " +
                    "is_ext_shared_channel: " + this.isExtSharedChannel + ", " +
                    "context_team_id: " + this.contextTeamId + ", " +
                    "context_enterprise_id: " + this.contextEnterpriseId +
                "}";
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    private static class Authorization {
        @JsonProperty("enterprise_id")
        private String enterpriseId;
        @JsonProperty("team_id")
        private String teamId;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("is_bot")
        private boolean isBot;
        @JsonProperty("is_enterprise_install")
        private boolean isEnterpriseInstall;

        public String getEnterpriseId() {
            return enterpriseId;
        }

        public void setEnterpriseId(String enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isBot() {
            return isBot;
        }

        public void setBot(boolean bot) {
            isBot = bot;
        }

        public boolean isEnterpriseInstall() {
            return isEnterpriseInstall;
        }

        public void setEnterpriseInstall(boolean enterpriseInstall) {
            isEnterpriseInstall = enterpriseInstall;
        }

        @Override
        public String toString(){
            return "{" +
                        "enterprise_id: " + this.enterpriseId + ", " +
                        "team_id: " + this.teamId + ", " +
                        "user_id: " + this.userId + ", " +
                        "is_bot: " + this.isBot + ", " +
                        "is_enterprise_install: " + this.isEnterpriseInstall +
                    "}";
        }
    }
}
