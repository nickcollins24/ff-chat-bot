package ncollins.model.chat.slack;

/**
 * {
 *     "ok": true,
 *     "user": USER
 * }
 */
public class GetUserResponse {
    private boolean ok;
    private SlackUser user;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public SlackUser getUser() {
        return user;
    }

    public void setUser(SlackUser user) {
        this.user = user;
    }

    public String toString(){
        return "{" +
                "ok: " + this.ok + ", " +
                "user: " + this.user +
                "}";
    }
}
