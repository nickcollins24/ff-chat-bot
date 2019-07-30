package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.model.chat.ImagePayload;
import ncollins.model.chat.MentionPayload;
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
    private static final int MAX_MESSAGE_LENGTH = 1000;

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

    /**
     * Send message to group from this bot.
     *
     * Note: max char[] length of message is 1000
     */
    public void sendMessage(String text, String attachmentPayload) {
        // replace ["] with [\"], otherwise message doesnt send.
        text = text.replaceAll("(?<![\\\\])\"","\\\\\"");

        // split text into chunks of length <= MAX_MESSAGE_LENGTH and send each chunk separately
        List<String> texts = splitMessage(text);
        for(String t : texts){
            logger.info(botName + " response length: " + t.toCharArray().length);

            String payload = "{" +
                    "\"bot_id\": \"" + botId + "\"," +
                    "\"text\": \"" + t + "\"," +
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

    /**
     * Splits message into chunks, ensuring that no chunk exceeds MAX_MESSAGE_LENGTH characters.
     * This is to avoid exceeding GroupMe's character limit, which results in a failed send.
     * @param text
     * @return
     */
    private List<String> splitMessage(String text){
        List<String> messages = new ArrayList();

        if(text.toCharArray().length <= MAX_MESSAGE_LENGTH){
            messages.add(text);
            return messages;
        }

        while(text.toCharArray().length > MAX_MESSAGE_LENGTH){
            int index = text.lastIndexOf("\\n", MAX_MESSAGE_LENGTH);
            messages.add(text.substring(0, index+2));
            text = text.substring(index+2);
        }
        messages.add(text);

        return messages;
    }
}
