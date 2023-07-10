package ncollins.chat.bots.slack;

import com.google.gson.Gson;
import ncollins.chat.bots.Bot;
import ncollins.helpers.StringHelpers;
import ncollins.model.chat.ProcessResult;
import ncollins.model.chat.slack.MessageResponse;
import ncollins.model.chat.slack.ReactionType;
import ncollins.model.espn.Season;
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
    private static final String SLACK_BOT_POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private static final String SLACK_BOT_GET_MESSAGE_URL = "https://slack.com/api/conversations.history";
    private static final String SLACK_BOT_GET_MESSAGE_IN_THREAD_URL = "https://slack.com/api/conversations.replies";
    private static final int MAX_MESSAGE_LENGTH = 40000;

    private HttpClient client = HttpClient.newHttpClient();
    private Gson gson = new Gson();

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
                    .uri(URI.create(SLACK_BOT_POST_MESSAGE_URL))
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
                .uri(URI.create(SLACK_BOT_POST_MESSAGE_URL))
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

    private MessageResponse getMessage(boolean inThread, String messageId, String channelId){
        String uri = inThread ?
                SLACK_BOT_GET_MESSAGE_IN_THREAD_URL + "?" +
                        "channel=" + channelId + "&ts=" + messageId + "&inclusive=true&limit=1" :
                SLACK_BOT_GET_MESSAGE_URL + "?" +
                "channel=" + channelId + "&latest=" + messageId + "&inclusive=true&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getAuthToken())
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), MessageResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void mockMessage(String messageId, String channelId, String threadId){
        // get message
        MessageResponse messageResponse = getMessage(false, messageId, channelId);

        // message is a reply, retrieve in thread
        if(messageResponse.getMessages().length > 0 &&
                !messageResponse.getMessages()[0].getTs().equals(messageId)){
            messageResponse = getMessage(true, messageId, channelId);
        }

        // mock message
        if(messageResponse != null &&
                messageResponse.getMessages() != null &&
                messageResponse.getMessages().length > 0 &&
                messageResponse.getMessages()[0].getTs().equals(messageId)){


            String messageFormatted = messageResponse.getMessages()[0].getText().replaceAll("<@.*>","");
            String mockedMessage =
                    StringHelpers.mockString(messageFormatted) +
                            " :" + ReactionType.MOCK.toString().toLowerCase() + ":";

            sendMessage(mockedMessage, channelId, threadId);
        }
    }
}
