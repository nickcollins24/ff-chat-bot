package ncollins.model.chat.slack;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *     "ok": true,
 *     "latest": "1688925482.075569",
 *     "messages": [
 *           { ... }
 *     ],
 *     "has_more": true,
 *     "pin_count": 4,
 *     "channel_actions_ts": null,
 *     "channel_actions_count": 0,
 *     "response_metadata": {
 *         "next_cursor": "bmV4dF90czoxNjg4OTI1MTg0NTk4MTI5"
 *     }
 * }
 */
public class MessageResponse {
    private String latest;
    private Event[] messages;

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public Event[] getMessages() {
        return messages;
    }

    public void setMessages(Event[] messages) {
        this.messages = messages;
    }
}
