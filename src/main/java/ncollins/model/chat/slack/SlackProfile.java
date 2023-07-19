package ncollins.model.chat.slack;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *      "real_name": "John Torres",
 *      "display_name": "Jan",
 *      "image_original": "https://avatars.slack-edge.com/2023-06-23/5480479887348_c5a2e6785a9ce21f8d0c_original.jpg",
 *      "first_name": "John",
 *      "last_name": "Torres",
 *      "team": "T05DMEU62AK"
 * }
 */
public class SlackProfile {
    @JsonProperty("real_name")
    private String realName;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("image_original")
    private String imageOriginal;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("team")
    private String team;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageOriginal() {
        return imageOriginal;
    }

    public void setImageOriginal(String imageOriginal) {
        this.imageOriginal = imageOriginal;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String toString(){
        return "{" +
                "realName: " + this.realName + ", " +
                "displayName: " + this.displayName + ", " +
                "imageOriginal: " + this.imageOriginal + ", " +
                "firstName: " + this.firstName + ", " +
                "lastName: " + this.lastName + ", " +
                "team: " + this.team +
                "}";
    }
}
