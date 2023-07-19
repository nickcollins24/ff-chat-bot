package ncollins.model.chat.slack;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *    "id": "U05DZ34JLAZ",
 *    "team_id": "T05DMEU62AK",
 *    "name": "jt92536",
 *    "real_name": "John Torres",
 *    "profile": PROFILE,
 *    "is_bot": false,
 *    "is_app_user": false
 *  }
 */
public class SlackUser {
    private String id;
    @JsonProperty("team_id")
    private String teamId;
    private String name;
    @JsonProperty("real_name")
    private String realName;
    private SlackProfile profile;
    @JsonProperty("is_bot")
    private boolean isBot;
    @JsonProperty("is_app_user")
    private boolean isAppUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public SlackProfile getProfile() {
        return profile;
    }

    public void setProfile(SlackProfile profile) {
        this.profile = profile;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bat) {
        isBot = bat;
    }

    public boolean isAppUser() {
        return isAppUser;
    }

    public void setAppUser(boolean appUser) {
        isAppUser = appUser;
    }

    public String toString(){
        return "{" +
                "id: " + this.id + ", " +
                "teamId: " + this.teamId + ", " +
                "name: " + this.name + ", " +
                "realName: " + this.realName + ", " +
                "profile: " + this.profile + ", " +
                "isAppUser: " + this.isAppUser + ", " +
                "isBot: " + this.isBot +
                "}";
    }
}
