package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.espn.EspnMessageBuilder;
import ncollins.gif.GifGenerator;
import ncollins.salt.SaltGenerator;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroupMeBot implements ChatBot {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String BOT_ID = "7f608eb2e1c2b036fe461a4765";
    private static final String BOT_NAME = "testbot";
    private static final String GROUP_ID = "43518373";
    private static final String USER_ID = "27277860";
    private static final String BOT_KEYWORD = "@" + BOT_NAME;
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";
    private HttpClient client;
    private GifGenerator gifGenerator = new GifGenerator();
    private SaltGenerator saltGenerator = new SaltGenerator();
    private EspnMessageBuilder espnMessageBuilder = new EspnMessageBuilder();

    public GroupMeBot(){
        this.client = HttpClient.newHttpClient();
    }

    public String getBotGroupId() {
        return GROUP_ID;
    }

    public String getBotUserId() {
        return USER_ID;
    }

    @Override
    public void processResponse(String fromUser, String text, String[] imageUrls) {
        logger.info(BOT_NAME + " is processing request: " + text);
        text = text.toLowerCase();

        if(text.startsWith(BOT_KEYWORD))
            processBotResponse(text.replace(BOT_KEYWORD, "").trim());
        else processEasterEggResponse(text);
    }

    private void processBotResponse(String text){
        if(text.matches("^$"))
            sendMessage(buildHelpMessage());
        else if(text.matches("^help$"))
            sendMessage(buildShowCommandsMessage());
        else if(text.startsWith("gif "))
            sendMessage(buildGifMessage(text.replace("gif","").trim()));
        else if(text.startsWith("salt "))
            sendMessage(buildSaltMessage(text.replace("salt","").trim()));
        else if(text.startsWith("show "))
            processEspnResponse(text.replace("show","").trim());
    }

    private void processEspnResponse(String text){
        if(text.startsWith("scores "))
            sendMessage(espnMessageBuilder.buildScoresMessage(null,0));
        else if(text.startsWith("matchups "))
            sendMessage(espnMessageBuilder.buildMatchupsMessage(null,null));
        else if(text.startsWith("standings "))
            sendMessage(espnMessageBuilder.buildStandingsMessage());
        else if(text.startsWith("points "))
            sendMessage(espnMessageBuilder.buildPointsMessage(null,0,null,0));
        else if(text.startsWith("players "))
            sendMessage(espnMessageBuilder.buildPlayersMessage(null,0,null));
        else if(text.startsWith("streaks "))
            sendMessage(espnMessageBuilder.buildOutcomeStreakMessage(null,0));
        else if(text.equals("jujus"))
            sendMessage(espnMessageBuilder.buildJujusMessage());
        else if(text.equals("salties"))
            sendMessage(espnMessageBuilder.buildSaltiesMessage());
        else if(text.matches("^blowouts($|\\s\\d+)")){
            String countStr = text.replaceAll("\\D+","");
            int count = countStr.isEmpty() ? 10 : Integer.parseInt(countStr);
            sendMessage(espnMessageBuilder.buildBlowoutsMessage(count));
        } else if(text.matches("^heartbreaks($|\\s\\d+)")){
            String countStr = text.replaceAll("\\D+","");
            int count = countStr.isEmpty() ? 10 : Integer.parseInt(countStr);
            sendMessage(espnMessageBuilder.buildHeartbreaksMessage(count));
        }
    }

    private void processEasterEggResponse(String text){
        if(text.contains("wonder"))
            sendMessage("did somebody say wonder?!");
        else if(text.contains("same"))
            sendMessage("same");
        else if(text.contains("gattaca"))
            sendMessage(gifGenerator.getRandomGif("rafi gattaca"));
        else if(text.matches(".+ de[a]?d$")){
            sendMessage("", "https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview");
        }
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
                "\"bot_id\": \"" + BOT_ID + "\"," +
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

    private String buildHelpMessage(){
        return "you rang? type '" + BOT_KEYWORD + " help' to see what i can do.";
    }

    private String buildShowCommandsMessage(){
        return "commands:\\n" +
                BOT_KEYWORD + " help -- show bot commands\\n" +
                BOT_KEYWORD + " gif [SOMETHING] -- post a random gif of something\\n";
    }

    private String buildGifMessage(String query){
        return gifGenerator.getRandomGif(query);
    }

    private String buildSaltMessage(String recipient) {
        return saltGenerator.throwSalt(recipient);
    }
}
