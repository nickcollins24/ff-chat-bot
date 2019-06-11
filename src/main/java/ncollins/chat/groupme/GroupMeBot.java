package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.espn.EspnMessageBuilder;
import ncollins.gif.GifGenerator;
import ncollins.salt.SaltGenerator;
import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Year;

public class GroupMeBot implements ChatBot {
    private HttpClient client;
    private final String botId;
    private final String botName;
    private final String botGroupId;
    private final String botUserId;
    private final String botKeyword;
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";
    private GifGenerator gifGenerator = new GifGenerator();
    private SaltGenerator saltGenerator = new SaltGenerator();
    private EspnMessageBuilder espnMessageBuilder = new EspnMessageBuilder();

    public GroupMeBot(String botId, String botName, String botGroupId, String botUserId){
        this.botId = botId;
        this.botName = botName;
        this.botGroupId = botGroupId;
        this.botUserId = botUserId;
        this.botKeyword = "@" + this.botName;
        this.client = HttpClient.newHttpClient();
    }

    public String getBotGroupId() {
        return botGroupId;
    }

    public String getBotUserId() {
        return botUserId;
    }

    @Override
    public void processResponse(String fromUser, String text, String[] imageUrls) {
        text = text.toLowerCase();

        if(text.startsWith(botKeyword))
            processBotResponse(text.replace(botKeyword, "").trim());
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
            sendMessage(espnMessageBuilder.buildScoresMessage(text.replace("scores","").trim()));
        else if(text.startsWith("matchups "))
            sendMessage(espnMessageBuilder.buildMatchupsMessage(text.replace("matchups","").trim()));
        else if(text.startsWith("standings "))
            sendMessage(espnMessageBuilder.buildStandingsMessage(text.replace("standings","").trim()));
        else if(text.startsWith("points "))
            sendMessage(espnMessageBuilder.buildPointsMessage(text.replace("points","").trim()));
        else if(text.startsWith("players "))
            sendMessage(espnMessageBuilder.buildPlayersMessage(text.replace("players","").trim()));
        else if(text.startsWith("streaks "))
            sendMessage(espnMessageBuilder.buildStreaksMessage(text.replace("scores","").trim()));
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
            int year = Year.now().getValue();
            String[] images = {"https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview"};
            sendMessage(year + "-" + year, images);
        }
    }

    public void sendMessage(String text){
        sendMessage(text, ArrayUtils.EMPTY_STRING_ARRAY);
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

    private String buildHelpMessage(){
        return "you rang? type '" + botKeyword + " help' to see what i can do.";
    }

    private String buildShowCommandsMessage(){
        return "commands:\\n" +
                botKeyword + " help -- show bot commands\\n" +
                botKeyword + " gif [SOMETHING] -- post a random gif of something\\n";
    }

    private String buildGifMessage(String query){
        return gifGenerator.getRandomGif(query);
    }

    private String buildSaltMessage(String recipient) {
        return saltGenerator.throwSalt(recipient);
    }
}
