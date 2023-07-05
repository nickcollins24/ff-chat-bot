package ncollins.chat.bots.slack;

import ncollins.model.chat.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SlackBot {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SLACK_BOT_URL = "https://slack.com/api/chat.postMessage";
    private static final int MAX_MESSAGE_LENGTH = 40000;

    private String authToken;
    private String botId;
    private String botName;
    private String botKeyword;
    private String botMention;
    private HttpClient client = HttpClient.newHttpClient();

    public SlackBot(String authToken,
                    String botId,
                    String botName){
        this.authToken = authToken;
        this.botId = botId;
        this.botName = botName;
        this.botKeyword = "@" + botName;
        this.botMention = "<@" + botId + ">";
    }

    public String getBotUserId() {
        return botId;
    }

    public String getBotKeyword() {
        return botKeyword;
    }

    public String getBotMention(){
        return botMention;
    }

    public String getBotName(){
        return botName;
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
        List<String> texts = splitMessage(text);
        for(String t : texts){
            logger.info("Bot(id: " + botId + ") " + "response length: " + t.toCharArray().length);

            String payload = "{" +
                    "\"channel\": \"" + channelId + "\"," +
                    "\"thread_ts\": \"" + threadId + "\"," +
                    "\"text\": \"" + text + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SLACK_BOT_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + authToken)
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
     * Send imager to group from this bot.
     */
    private void sendImage(String imageUrl, String channelId, String threadId) {
        logger.info("Bot(id: " + botId + ") " + "response length: " + imageUrl.toCharArray().length);

        String payload = "{" +
                "\"channel\": \"" + channelId + "\"," +
                "\"thread_ts\": \"" + threadId + "\"," +
                "\"blocks\":" + "[" +
                    "{" +
                        "\"type\": \"image\"," +
                        "\"image_url\": \"" + imageUrl + "\"," +
                        "\"alt_text\": \"image/gif\"" +
                    "}" +
                "]}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SLACK_BOT_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
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

//    {
//        "channel": "C05EBSXBGCB",
//            "thread_ts": "",
//            "blocks": [
//        {
//            "type": "image",
//                "image_url": "https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview",
//                "alt_text": "image/gif"
//        }
//    ]
//    }

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
