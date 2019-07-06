package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroupMeBot implements ChatBot {
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";

    private String botId;
    private String botName;
    private String botKeyword;
    private String groupId;
    private String userId;
    private HttpClient client;

    public GroupMeBot(String botId, String botName, String groupId, String userId){
        this.botId = botId;
        this.botName = botName;
        this.botKeyword = "@" + botName;
        this.groupId = groupId;
        this.userId = userId;
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

    public void sendMessage(String text){
        sendMessage(text, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public void sendMessage(String text, String imageUrl){
        String[] imageUrls = {imageUrl};
        sendMessage(text, imageUrls);
    }

    @Override
    public void sendMessage(String text, String[] imageUrls) {
        String attachments = buildAttachmentsPayload(imageUrls);
        String payload = "{" +
                "\"bot_id\": \"" + botId + "\"," +
                "\"text\": \"" + text + "\"," +
                "\"attachments\": " + attachments + "}";

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

    private String buildAttachmentsPayload(String[] imageUrls){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String imageUrl : imageUrls){
            sb.append("{\"type\": \"image\",\"url\": \"" + imageUrl + "\"}");
        }
        sb.append("]");

        return sb.toString();
    }
}
