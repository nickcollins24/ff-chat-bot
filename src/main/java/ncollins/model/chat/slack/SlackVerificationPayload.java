package ncollins.model.chat.slack;

public class SlackVerificationPayload {
    private String token;
    private String challenge;
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "{" +
                "token: " + this.getToken() + ", " +
                "challenge: " + this.getChallenge() + ", " +
                "type: " + this.getType() +
                "}";
    }
}
