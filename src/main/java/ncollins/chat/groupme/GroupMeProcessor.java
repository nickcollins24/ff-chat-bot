package ncollins.chat.groupme;

import ncollins.chat.ChatBotProcessor;
import ncollins.data.PinCollection;
import ncollins.gif.GifGenerator;
import ncollins.model.Order;
import ncollins.model.chat.ChatResponse;
import ncollins.model.chat.Emojis;
import ncollins.model.chat.ImagePayload;
import ncollins.model.chat.Pin;
import ncollins.model.espn.Outcome;
import ncollins.espn.EspnMessageBuilder;
import ncollins.magiceightball.MagicAnswerGenerator;
import ncollins.model.espn.Position;
import ncollins.salt.SaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.regex.Matcher;

public class GroupMeProcessor implements ChatBotProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private GroupMeBot mainBot;
    private GroupMeBot espnBot;
    private EspnMessageBuilder espnMessageBuilder;
    private GifGenerator gifGenerator;
    private SaltGenerator saltGenerator = new SaltGenerator();
    private MagicAnswerGenerator answerGenerator = new MagicAnswerGenerator();
    private PinCollection pinCollection;

    public GroupMeProcessor(GroupMeBot mainBot, GroupMeBot espnBot, PinCollection pinCollection,
                            EspnMessageBuilder espnMessageBuilder, GifGenerator gifGenerator){
        this.mainBot = mainBot;
        this.espnBot = espnBot;
        this.pinCollection = pinCollection;
        this.espnMessageBuilder = espnMessageBuilder;
        this.gifGenerator = gifGenerator;
    }

    public GroupMeBot getMainBot(){
        return mainBot;
    }

    private GroupMeBot getEspnBot(){
        return espnBot;
    }

    @Override
    public void processResponse(String fromUser, String text, ChatResponse.Attachment[] attachments, long currentTime) {
        logger.info("Incoming message: " + text);

        text = text.toLowerCase();

        if(text.contains("@here"))
            getMainBot().sendMessageWithMention("@here " + Emojis.EYES_LEFT + Emojis.FINGER_UP, new int[]{0,5});
        if(text.startsWith("#pin ") || text.endsWith(" #pin") || text.contains(" #pin ")){
            String textEdited = text.replaceAll("\n", Matcher.quoteReplacement("\\n"))
                    .replaceAll("#pin", "");

            pinCollection.addPin(new Pin(textEdited, fromUser, currentTime));
        }

        if(text.startsWith(getMainBot().getBotKeyword()))
            processBotResponse(text.replace(getMainBot().getBotKeyword(), "").trim());
        else processEasterEggResponse(text);
    }

    private void processBotResponse(String text){
        if(text.matches("^$"))
            getMainBot().sendMessage(buildHelpMessage());
        else if(text.matches("^help$"))
            getMainBot().sendMessage(buildShowCommandsMessage());
        else if(text.startsWith("gif "))
            getMainBot().sendMessage(buildGifMessage(text.replace("gif","").trim()));
        else if(text.startsWith("salt "))
            getMainBot().sendMessage(buildSaltMessage(text.replace("salt","").trim()));
        else if(text.equals("show pins")){
            if(pinCollection.getPins().isEmpty())
                getMainBot().sendMessage("add #pin to any message to pin it");
            else getMainBot().sendMessage(buildPinsMessage());
        } else if(text.matches("^delete pin \\d*$")){
            int index =   Integer.parseInt(text.replaceAll("\\D+",""));
            if(0 > index || index >= pinCollection.getPins().size())
                getMainBot().sendMessage("pick a valid id");
            else pinCollection.deletePin(index);
        } else if(text.startsWith("show "))
            processEspnResponse(text.replace("show","").trim());
        else if(text.endsWith("?"))
            getMainBot().sendMessage(buildMagicAnswerMessage());
    }

    //TODO: add in cases for "all-time", "current year", and "a given year"
    private void processEspnResponse(String text){
        // {top|bottom} [TOTAL] scores {ever|YEAR|}
        if(text.matches("(top|bottom) \\d* ?scores(\\sever|\\s\\d+|)$")) {
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;

            String[] textSplit = text.split("scores");
            String totalStr = textSplit[0].replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            String yearStr = textSplit.length == 1 ? "" :
                    textSplit[1].replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                getEspnBot().sendMessage(espnMessageBuilder.buildScoresMessage(order, total, false));
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(espnMessageBuilder.buildScoresMessage(order, total, null, seasonId,false));
            // current season
            } else {
                getEspnBot().sendMessage(espnMessageBuilder.buildScoresMessageCurrentYear(order, total,false));
            }
        // {top|bottom} [TOTAL] records {ever|YEAR|}
        } else if(text.matches("(top|bottom) \\d* ?records(\\sever|\\s\\d+|)$")){
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;

            String[] textSplit = text.split("records");
            String totalStr = textSplit[0].replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            String yearStr = textSplit.length == 1 ? "" :
                    textSplit[1].replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                getEspnBot().sendMessage(espnMessageBuilder.buildRecordsMessage(order, total));
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(espnMessageBuilder.buildRecordsMessage(order, total, seasonId));
            // current season
            } else {
                getEspnBot().sendMessage(espnMessageBuilder.buildRecordsMessageCurrentYear(order, total));
            }
        // standings {ever|YEAR|}
        } else if(text.matches("standings(\\sever|\\s\\d+|)$")){
            String yearStr = text.replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                getEspnBot().sendMessage(espnMessageBuilder.buildStandingsMessage());
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(espnMessageBuilder.buildStandingsMessage(seasonId));
            // current season
            } else {
                getEspnBot().sendMessage(espnMessageBuilder.buildStandingsMessageCurrentYear());
            }
        // {top|bottom} [TOTAL] [POSITION|players] {WEEK|}
        } else if(text.matches("(top|bottom) \\d* ?([a-zA-Z]+|players)$")) {
            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);

            Position position;
            try {
                String positionStr = text.replaceAll("(top|bottom)", "").
                        replaceAll("\\d+", "").trim();
                position = positionStr.equals("players") ? null : Position.valueOf(positionStr.toUpperCase());
            } catch(Exception e){
                getEspnBot().sendMessage("enter a valid position [qb,rb,wr,te,k,d,flex]");
                return;
            }

            getEspnBot().sendMessage(espnMessageBuilder.buildPlayersMessageByCurrentWeek(order, total, position));
        // {top|bottom} [TOTAL] {pf|wins|losses} through [WEEK]
//        } else if(text.matches("(top|bottom)(\\s\\d)* (pf|wins|losses) through \\d+$")) {
//            Order order = text.startsWith("top") ? Order.DESC : Order.ASC;
//            Outcome outcome = text.contains(" wins ") ?
//                    Outcome.WIN :
//                    text.contains(" losses ") ? Outcome.LOSS : null;
//
//            String[] textSplit = text.split("through");
//            String totalStr = textSplit[0].replaceAll("\\D+", "");
//            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
//            String weekStr = textSplit[1].replaceAll("\\D+", "");
//            int week = Integer.parseInt(weekStr);
//
//            if(outcome == null){
//                getEspnBot().sendMessage(espnMessageBuilder.buildPointsThroughMessage(order, total, week));
//            } else {
//                getEspnBot().sendMessage(espnMessageBuilder.buildOutcomeThroughMessage(order, outcome, total, week));
//            }

        // top [TOTAL] pf streaks
        } else if(text.matches("top(\\s\\d)* pf streaks$")) {
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(espnMessageBuilder.buildPointsStreakMessage(total));
        // [TOTAL] {win|loss} streaks
        } else if(text.matches("\\d* ?(win|loss) streaks$")) {
            Outcome outcome = text.contains(" win ") ? Outcome.WIN : Outcome.LOSS;
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(espnMessageBuilder.buildOutcomeStreakMessage(outcome, total));
        // [TOTAL] blowouts
        } else if(text.matches("(^|\\d+\\s)blowouts$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(espnMessageBuilder.buildBlowoutsMessage(total));
        // [TOTAL] heartbreaks
        } else if(text.matches("(^|\\d+\\s)heartbreaks$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(espnMessageBuilder.buildHeartbreaksMessage(total));
        // matchups [TEAM1] [TEAM2]
        } else if(text.matches("matchups \\S+ \\S+$")) {
            String[] teams = text.split("\\s");
            getEspnBot().sendMessage(espnMessageBuilder.buildMatchupsMessage(teams[1], teams[2]));
        // pf winners
        } else if(text.equals("pf winners")){
            getEspnBot().sendMessage(espnMessageBuilder.buildWeeklyPfWinners());
        // jujus
        } else if(text.equals("jujus"))
            getEspnBot().sendMessage(espnMessageBuilder.buildJujusMessage());
        // salties
        else if(text.equals("salties"))
            getEspnBot().sendMessage(espnMessageBuilder.buildSaltiesMessage());
    }

    private void processEasterEggResponse(String text){
        if(text.contains("wonder"))
            getMainBot().sendMessage("https://houseofgeekery.files.wordpress.com/2013/05/tony-wonder-arrested-development-large-msg-132259950538.jpg");
        else if(text.equals("same"))
            getMainBot().sendMessage("https://media1.tenor.com/images/7c981c036a7ac041e66b0c87b42542f2/tenor.gif");
        else if(text.contains("gattaca"))
            getMainBot().sendMessage(gifGenerator.search("rafi gattaca"));
        else if(text.matches(".+ de[a]?d$")){
            getMainBot().sendMessage("", new ImagePayload("https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview"));
        } else if(text.contains("woof")){
            getMainBot().sendMessage(gifGenerator.search("corgi"));
        }
    }

    private String buildHelpMessage(){
        return "you rang? type '" + getMainBot().getBotKeyword() + " help' to see what i can do.";
    }

    private String buildShowCommandsMessage(){
        return "commands:\\n" +
                "@here -- sends a mention notification to group\\n" +
                " #pin -- pin a message to view later\\n" +
                getMainBot().getBotKeyword() + " help -- show bot commands\\n" +
                getMainBot().getBotKeyword() + " gif [SOMETHING] -- post a random gif of something\\n" +
                getMainBot().getBotKeyword() + " salt [SOMEONE] -- throw salt at someone\\n" +
                getMainBot().getBotKeyword() + " [QUESTION]? -- ask a yes/no question\\n" +
                getMainBot().getBotKeyword() + " show pins -- show all pinned messages\\n" +
                getMainBot().getBotKeyword() + " delete pin [INDEX] -- delete a pinned message\\n" +
                getMainBot().getBotKeyword() + " show {top|bottom} [TOTAL] scores {ever|YEAR|} -- top/bottom scores\\n" +
                getMainBot().getBotKeyword() + " show {top|bottom} [TOTAL] records {ever|YEAR|} -- top/bottom records\\n" +
                getMainBot().getBotKeyword() + " show {top|bottom} [TOTAL] pf through [WEEK] -- top/bottom pf through given week\\n" +
                getMainBot().getBotKeyword() + " show {top|bottom} [TOTAL] {wins/losses} through [WEEK] -- top/bottom w/l through given week\\n" +
                getMainBot().getBotKeyword() + " show {top|bottom} [TOTAL] {POSITION|players} {WEEK|} -- best/worst players\\n" +
                getMainBot().getBotKeyword() + " show [TOTAL] blowouts -- biggest wins\\n" +
                getMainBot().getBotKeyword() + " show [TOTAL] heartbreaks -- closest losses\\n" +
                getMainBot().getBotKeyword() + " show standings {ever|YEAR|} -- standings\\n" +
                getMainBot().getBotKeyword() + " show matchups [TEAM1] [TEAM2] -- matchup stats between two teams\\n" +
                getMainBot().getBotKeyword() + " show pf winners -- list of most pf per week\\n" +
                getMainBot().getBotKeyword() + " show jujus -- all time jujus\\n" +
                getMainBot().getBotKeyword() + " show salties -- all time salties";
    }

    private String buildGifMessage(String query){
        return gifGenerator.search(query);
    }

    private String buildSaltMessage(String recipient) {
        return saltGenerator.throwSalt(recipient);
    }

    private String buildMagicAnswerMessage(){
        return answerGenerator.getRandom();
    }

    private String buildPinsMessage(){
        List<Pin> pins = pinCollection.getPins();

        StringBuilder sb = new StringBuilder();
        for(int i=0; i < pins.size(); i++){
            sb.append(pins.get(i).toString() + " (id:" + i + ")\\n\\n");
        }

        return sb.toString();
    }
}
