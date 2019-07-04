package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.espn.EspnMessageBuilder;
import ncollins.gif.GifGenerator;
import ncollins.magiceightball.MagicAnswerGenerator;
import ncollins.model.Order;
import ncollins.model.espn.Outcome;
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

    private static final String BOT_ID = System.getenv("BOT_ID");
    private static final String BOT_NAME = System.getenv("BOT_NAME");
    private static final String GROUP_ID = System.getenv("GROUP_ME_GROUP_ID");
    private static final String USER_ID = System.getenv("GROUP_ME_USER_ID");
    private static final String BOT_KEYWORD = "@" + BOT_NAME;
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";

    private HttpClient client;
    private GifGenerator gifGenerator = new GifGenerator();
    private SaltGenerator saltGenerator = new SaltGenerator();
    private MagicAnswerGenerator answerGenerator = new MagicAnswerGenerator();
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
        text = text.toLowerCase();

        if(text.startsWith(BOT_KEYWORD))
            processBotResponse(text.replace(BOT_KEYWORD, "").trim());
        else processEasterEggResponse(text);
    }

    private void processBotResponse(String text){
        logger.info(BOT_NAME + " is processing request: " + text);

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
        else if(text.endsWith("?"))
            sendMessage(buildMagicAnswerMessage());
    }

    private void processEspnResponse(String text){
        // {top|bottom} [TOTAL] scores
        if(text.matches("(top|bottom) \\d* ?scores$")){
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            sendMessage(espnMessageBuilder.buildScoresMessage(order,total));
        // {top|bottom} [TOTAL] [POSITION|players]
        } else if(text.matches("(top|bottom) \\d* ?players$")) {
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            sendMessage(espnMessageBuilder.buildPlayersMessage(order, total, null));
        // [TOTAL] {win|loss} streaks
        } else if(text.matches("\\d* ?(win|loss) streaks$")) {
            Outcome outcome = text.contains(" win ") ? Outcome.WIN : Outcome.TIE;
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            sendMessage(espnMessageBuilder.buildOutcomeStreakMessage(outcome, total));
        // [TOTAL] blowouts
        } else if(text.matches("(^|\\s\\d+)blowouts$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            sendMessage(espnMessageBuilder.buildBlowoutsMessage(total));
        // [TOTAL] heartbreaks
        } else if(text.matches("(^|\\s\\d+)heartbreaks$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            sendMessage(espnMessageBuilder.buildHeartbreaksMessage(total));
        // matchups
        } else if(text.equals("matchups"))
            sendMessage(espnMessageBuilder.buildMatchupsMessage());
        // standings
        else if(text.equals("standings"))
            sendMessage(espnMessageBuilder.buildStandingsMessage());
        // jujus
        else if(text.equals("jujus"))
            sendMessage(espnMessageBuilder.buildJujusMessage());
        // salties
        else if(text.equals("salties"))
            sendMessage(espnMessageBuilder.buildSaltiesMessage());
    }

    private void processEasterEggResponse(String text){
        if(text.contains("wonder"))
            sendMessage("https://houseofgeekery.files.wordpress.com/2013/05/tony-wonder-arrested-development-large-msg-132259950538.jpg");
        else if(text.contains("same"))
            sendMessage("https://media1.tenor.com/images/7c981c036a7ac041e66b0c87b42542f2/tenor.gif");
        else if(text.contains("gattaca"))
            sendMessage(gifGenerator.translateGif("rafi gattaca"));
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
                BOT_KEYWORD + " gif [SOMETHING] -- post a random gif of something\\n" +
                BOT_KEYWORD + " salt [SOMEONE] -- throw salt at someone\\n" +
                BOT_KEYWORD + " show {top|bottom} [TOTAL] scores -- top/bottom scores this year\\n" +
                BOT_KEYWORD + " show matchups -- matchups for the current week\\n" +
                BOT_KEYWORD + " show standings -- standings this year\\n" +
                BOT_KEYWORD + " show {top|bottom} [TOTAL] [POSITION|players] -- best/worst players this year\\n" +
                BOT_KEYWORD + " show [TOTAL] {win|loss} streaks -- longest win/loss streaks this year\\n" +
                BOT_KEYWORD + " show jujus -- this years jujus\\n" +
                BOT_KEYWORD + " show salties -- this years salties\\n" +
                BOT_KEYWORD + " show [TOTAL] blowouts -- biggest blowouts this year\\n" +
                BOT_KEYWORD + " show [TOTAL] heartbreaks -- closest games this year";
    }

    private String buildGifMessage(String query){
        return gifGenerator.translateGif(query);
    }

    private String buildSaltMessage(String recipient) {
        return saltGenerator.throwSalt(recipient);
    }

    private String buildMagicAnswerMessage(){
        return answerGenerator.getRandom();
    }
}
