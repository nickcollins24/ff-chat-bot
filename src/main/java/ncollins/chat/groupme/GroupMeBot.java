package ncollins.chat.groupme;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ncollins.chat.ChatBot;
import ncollins.gif.GifGenerator;
import ncollins.model.chat.ImagePayload;
import ncollins.model.chat.MentionPayload;
import ncollins.model.chat.PollPayload;
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
    private static final String GROUP_ME_BOT_URL = "https://api.groupme.com/v3/bots/post";
    private static final String GROUP_ME_POLL_URL = "https://api.groupme.com/v3/poll";
    private static final int MAX_MESSAGE_LENGTH = 1000;

    private String accessToken;
    private String botId;
    private String botName;
    private String botKeyword;
    private String groupId;
    private String userId;
    private HttpClient client = HttpClient.newHttpClient();
    private GifGenerator gifGenerator;

    public GroupMeBot(GifGenerator gifGenerator,
                      String accessToken,
                      String botId,
                      String botName,
                      String groupId,
                      String userId){
        this.gifGenerator = gifGenerator;
        this.accessToken = accessToken;
        this.botId = botId;
        this.botName = botName;
        this.botKeyword = "@" + botName;
        this.groupId = groupId;
        this.userId = userId;
    }

    public GifGenerator getGifGenerator(){ return gifGenerator; }

    public String getBotGroupId() {
        return groupId;
    }

    public String getBotUserId() {
        return userId;
    }

    public String getBotKeyword() {
        return botKeyword;
    }

    public String getBotName() {
        return botName;
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
     * Send message to group from this bot that mentions(@) all users in the group.
     *
     * @param text message text to display
     * @param loci location of text that should be displayed in bold font
     */
    public void sendMessageWithMention(String text, int[] loci){
        sendMessage(text, buildMentionAllPayload(loci).toString());
    }

    public void sendPollMessage(PollPayload payload){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROUP_ME_POLL_URL + "/" + this.getBotGroupId() + "?token=" + this.accessToken))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            logger.error("Exception while sending poll message: " + e);
        }
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
                    .uri(URI.create(GROUP_ME_BOT_URL))
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

    private MentionPayload buildMentionAllPayload(int[] loci){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groupme.com/v3/groups/" + this.getBotGroupId() + "?token=" + this.accessToken))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JsonArray jsonArray = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("response").getAsJsonArray("members");

            int[] userIds = new int[jsonArray.size()];
            for(int i=0; i < jsonArray.size(); i++){
                userIds[i] = jsonArray.get(i).getAsJsonObject().get("user_id").getAsInt();
            }

            int[][] loci2D = new int[userIds.length][2];
            for(int i=0; i < userIds.length; i++){
                loci2D[i] = loci;
            }

            return new MentionPayload(userIds, loci2D);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
