package ncollins.chat.bots.slack;

import ncollins.chat.bots.Bot;
import ncollins.helpers.StringHelpers;
import ncollins.model.chat.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SlackBot extends Bot {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SLACK_BOT_URL = "https://slack.com/api/chat.postMessage";
    private static final int MAX_MESSAGE_LENGTH = 40000;

    private HttpClient client = HttpClient.newHttpClient();

    public SlackBot(String authToken,
                    String botId,
                    String botName){
        super(authToken, botId, botName);
        setBotMention("<@" + botId + ">");
    }

    @Override
    public void sendMessage(String text){
        sendMessage(text, getChatId(), "");
    }

    public void sendMessage(ProcessResult result, String channelId, String threadId){
        if(result.getText().isEmpty()) return;

        switch(result.getType()){
            case TEXT: sendMessage(result.getText(), channelId, threadId);
            case IMAGE: sendImage(result.getText(), channelId, threadId);
        }

    }

    /**
     * Send message to group from this bot.
     *
     * Note: max char[] length of message is 1000
     */
    private void sendMessage(String text, String channelId, String threadId) {
        // replace ["] with [\"], otherwise message doesnt send.
        text = text.replaceAll("(?<![\\\\])\"","\\\\\"");

        // split text into chunks of length <= MAX_MESSAGE_LENGTH and send each chunk separately
        List<String> texts = splitMessage(text, MAX_MESSAGE_LENGTH);
        for(String t : texts){
            logger.info("Bot(id: " + getBotId() + ") " + "response length: " + t.toCharArray().length);

            String payload = "{" +
                    "\"channel\": \"" + channelId + "\"," +
                    "\"thread_ts\": \"" + threadId + "\"," +
                    "\"text\": \"" + t + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SLACK_BOT_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + getAuthToken())
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

    @Override
    public void sendImage(String text, String imageUrl){
        sendImage(text, imageUrl, getChatId(), "");
    }

    private void sendImage(String imageUrl, String channelId, String threadId) {
        sendImage("", imageUrl, channelId, threadId);
    }

    /**
     * Send imager to group from this bot.
     */
    private void sendImage(String text, String imageUrl, String channelId, String threadId) {
        logger.info("Bot(id: " + getBotId() + ") " + "response length: " + imageUrl.toCharArray().length);

        String textBlock = "";
        if(text != null && !text.isEmpty()){
            textBlock = "{" +
                    "\"type\": \"section\"," +
                    "\"text\": {" +
                    "\"type\": \"plain_text\", \"text\": \"" + text + "\"" +
                    "}" +
            "},";
        }

        String payload = "{" +
                "\"channel\": \"" + channelId + "\"," +
                "\"thread_ts\": \"" + threadId + "\"," +
                "\"blocks\":" + "[" +
                    textBlock +
                    "{" +
                        "\"type\": \"image\"," +
                        "\"image_url\": \"" + imageUrl + "\"," +
                        "\"alt_text\": \"image/gif\"" +
                    "}" +
                "]}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SLACK_BOT_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getAuthToken())
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
