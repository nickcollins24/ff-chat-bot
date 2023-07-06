package ncollins.chat.bots.groupme;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ncollins.chat.bots.Bot;
import ncollins.model.chat.MentionPayload;
import ncollins.model.chat.PollPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GroupMeBot extends Bot {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GROUP_ME_BOT_URL = "https://api.groupme.com/v3/bots/post";
    private static final String GROUP_ME_POLL_URL = "https://api.groupme.com/v3/poll";
    private static final int MAX_MESSAGE_LENGTH = 1000;

    private HttpClient client = HttpClient.newHttpClient();

    public GroupMeBot(String accessToken,
                      String botId,
                      String botName,
                      String groupId){
        super(accessToken, botId, botName, groupId);
    }

    @Override
    public void sendMessage(String text){
        sendMessage(text, "[]");
    }

    @Override
    public void sendImage(String text, String imageUrl) {
        sendMessage(text);
        sendMessage(imageUrl);
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

    public void sendPoll(PollPayload payload){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROUP_ME_POLL_URL + "/" + getChatId() + "?token=" + getAuthToken()))
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
        List<String> texts = splitMessage(text, MAX_MESSAGE_LENGTH);
        for(String t : texts){
            logger.info(getBotName() + " response length: " + t.toCharArray().length);

            String payload = "{" +
                    "\"bot_id\": \"" + getBotId() + "\"," +
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

    private MentionPayload buildMentionAllPayload(int[] loci){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groupme.com/v3/groups/" + getChatId() + "?token=" + getAuthToken()))
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
