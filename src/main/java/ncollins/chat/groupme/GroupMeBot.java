package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.model.chat.ImagePayload;
import ncollins.model.chat.MentionPayload;
import ncollins.model.chat.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GroupMeBot implements ChatBot {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";

    private String botId;
    private String botName;
    private String botKeyword;
    private String groupId;
    private String userId;
    private List<Pin> pins;
    private HttpClient client;

    public GroupMeBot(String botId, String botName, String groupId, String userId){
        this.botId = botId;
        this.botName = botName;
        this.botKeyword = "@" + botName;
        this.groupId = groupId;
        this.userId = userId;
        this.pins = new ArrayList();
        this.client = HttpClient.newHttpClient();
    }

    public String getBotGroupId() {
        return groupId;
    }

    public String getBotUserId() {
        return userId;
    }

    public String getBotKeyword() {
        return botKeyword;
    }

    public List<Pin> getPins(){
        return pins;
    }

    public void addPin(Pin pin){
        pins.add(pin);
    }

    public void deletePin(int index){
        pins.remove(index);
    }

    @Override
    public void sendMessage(String text, ImagePayload payload){
        sendMessage(text, payload.toString());
    }

    @Override
    public void sendMessage(String text, MentionPayload payload){
        sendMessage(text, payload.toString());
    }

    public void sendMessage(String text){
        sendMessage(text, "[]");
    }

    public void sendMessage(String text, String attachmentPayload) {
        String payload = "{" +
                "\"bot_id\": \"" + botId + "\"," +
                "\"text\": \"" + text + "\"," +
                "\"attachments\": " + attachmentPayload + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROUP_ME_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
