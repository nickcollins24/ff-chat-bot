package ncollins.chat.groupme;

import ncollins.chat.ChatBotProcessor;
import ncollins.data.PinCollection;
import ncollins.model.Order;
import ncollins.model.chat.*;
import ncollins.model.espn.Outcome;
import ncollins.magiceightball.MagicAnswerGenerator;
import ncollins.model.espn.Position;
import ncollins.salt.SaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

@Component
public class GroupMeProcessor implements ChatBotProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String DRAFT_DAY = System.getenv("DRAFT_DAY");
    private static final String NFL_KICKOFF = System.getenv("NFL_KICKOFF");

    private MainGroupMeBot mainBot;
    private EspnGroupMeBot espnBot;
    private SaltGenerator saltGenerator;
    private MagicAnswerGenerator answerGenerator;
    private PinCollection pinCollection;

    @Autowired
    public GroupMeProcessor(MainGroupMeBot mainBot,
                            EspnGroupMeBot espnBot,
                            SaltGenerator saltGenerator,
                            MagicAnswerGenerator answerGenerator,
                            PinCollection pinCollection){
        this.mainBot = mainBot;
        this.espnBot = espnBot;
        this.saltGenerator = saltGenerator;
        this.answerGenerator = answerGenerator;
        this.pinCollection = pinCollection;
    }

    public MainGroupMeBot getMainBot(){
        return mainBot;
    }

    private EspnGroupMeBot getEspnBot(){
        return espnBot;
    }

    @Override
    public void processResponse(Subject subject, long currentTime) {
        logger.info("Incoming message: " + subject.getText());

        //send to bot for processing if message was created by a user (not bot) in the required group
        if(subject.getGroupId().equals(getMainBot().getBotGroupId()) && subject.getSenderType().equals("user")){
            String fromUser = subject.getName();
            String text = subject.getText().trim().toLowerCase();

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
        } else {
            logger.info("Skipping bot message: " + subject.getText());
        }
    }

    private void processBotResponse(String text){
        if(text.matches("^$"))
            getMainBot().sendMessage(buildHelpMessage());
        else if(text.matches("^help$"))
            getMainBot().sendMessage(buildShowCommandsMessage());
        else if(text.startsWith("gif "))
            getMainBot().sendMessage(getMainBot().getGifGenerator().search(text.replace("gif","").trim()));
        else if(text.startsWith("salt "))
            getMainBot().sendMessage(buildSaltMessage(text.replace("salt","").trim()));
        else if(text.equals("show countdown"))
            getMainBot().sendMessage(buildCountdown());
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
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildScoresMessage(order, total, false));
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildScoresMessage(order, total, null, seasonId,false));
            // current season
            } else {
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildScoresMessageCurrentYear(order, total,false));
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
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildRecordsMessage(order, total));
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildRecordsMessage(order, total, seasonId));
            // current season
            } else {
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildRecordsMessageCurrentYear(order, total));
            }
        // standings {ever|YEAR|}
        } else if(text.matches("standings(\\sever|\\s\\d+|)$")){
            String yearStr = text.replaceAll("\\D+", "");

            // history
            if(text.contains("ever")){
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildStandingsMessage());
            // specified season
            } else if(!yearStr.isEmpty()){
                Integer seasonId = Integer.parseInt(yearStr);
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildStandingsMessage(seasonId));
            // current season
            } else {
                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildStandingsMessageCurrentYear());
            }
        } else if(text.matches("playoff standings(\\sever|)$")){
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildPlayoffStandingsMessage());
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

            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildPlayersMessageByCurrentWeek(order, total, position));
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
//                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildPointsThroughMessage(order, total, week));
//            } else {
//                getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildOutcomeThroughMessage(order, outcome, total, week));
//            }

        // top [TOTAL] pf streaks
        } else if(text.matches("top(\\s\\d)* pf streaks$")) {
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildPointsStreakMessage(total));
        // [TOTAL] {win|loss} streaks
        } else if(text.matches("\\d* ?(win|loss) streaks$")) {
            Outcome outcome = text.contains(" win ") ? Outcome.WIN : Outcome.LOSS;
            String totalStr = text.replaceAll("\\D+", "");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildOutcomeStreakMessage(outcome, total));
        // [TOTAL] blowouts
        } else if(text.matches("(^|\\d+\\s)blowouts$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildBlowoutsMessage(total));
        // [TOTAL] heartbreaks
        } else if(text.matches("(^|\\d+\\s)heartbreaks$")){
            String totalStr = text.replaceAll("\\D+","");
            int total = totalStr.isEmpty() ? 10 : Integer.parseInt(totalStr);
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildHeartbreaksMessage(total));
        // matchups [TEAM1] [TEAM2]
        } else if(text.matches("matchups \\S+ \\S+$")) {
            String[] teams = text.split("\\s");
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildMatchupsMessage(teams[1], teams[2]));
        // sackos ever
        } else if(text.equals("sackos")){
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildSackosMessage());
        // champs ever
        } else if(text.equals("champs")){
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildChampsMessage());

        // pf winners
        } else if(text.equals("pf winners")){
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildWeeklyPfWinners());
        // jujus
        } else if(text.equals("jujus"))
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildJujusMessage());
        // salties
        else if(text.equals("salties"))
            getEspnBot().sendMessage(getEspnBot().getMessageBuilder().buildSaltiesMessage());
    }

    private void processEasterEggResponse(String text){
        if(text.contains("wonder"))
            getMainBot().sendMessage("https://houseofgeekery.files.wordpress.com/2013/05/tony-wonder-arrested-development-large-msg-132259950538.jpg");
        else if(text.equals("same"))
            getMainBot().sendMessage("https://media1.tenor.com/images/7c981c036a7ac041e66b0c87b42542f2/tenor.gif");
        else if(text.contains("gattaca"))
            getMainBot().sendMessage(getMainBot().getGifGenerator().search("rafi gattaca"));
        else if(text.matches(".+ de[a]?d$"))
            getMainBot().sendMessage("", new ImagePayload("https://i.groupme.com/498x278.gif.f652fb0c235746b3984a5a4a1a7fbedb.preview"));
        else if(text.contains("woof"))
            getMainBot().sendMessage(getMainBot().getGifGenerator().search("corgi"));
        else if(text.contains("olivia munn"))
            getMainBot().sendMessage(getMainBot().getGifGenerator().search("olivia munn"));
        else if(text.contains("boobs"))
            getMainBot().sendMessage(getMainBot().getGifGenerator().search("boobs"));
        else if(!text.contains(".com") && text.matches(".*6[.+*x/-]?9.*"))
            getMainBot().sendMessage("https://media.giphy.com/media/5xtDaruonEVJXvedMXu/giphy.gif");
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
                getMainBot().getBotKeyword() + " show playoff standings {ever|} -- playoff standings\\n" +
                getMainBot().getBotKeyword() + " show matchups [TEAM1] [TEAM2] -- matchup stats between two teams\\n" +
                getMainBot().getBotKeyword() + " show pf winners -- list of most pf per week\\n" +
                getMainBot().getBotKeyword() + " show jujus -- all time jujus\\n" +
                getMainBot().getBotKeyword() + " show salties -- all time salties\\n" +
                getMainBot().getBotKeyword() + " show champs -- all time champions\\n" +
                getMainBot().getBotKeyword() + " show sackos -- all time sackos";
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

    private String buildCountdown(){
        try {
            StringBuilder sb = new StringBuilder();

            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));

            final long millisTilDraft = dateFormat.parse(DRAFT_DAY).getTime() - System.currentTimeMillis();
            final long millisTilSeasonStart = dateFormat.parse(NFL_KICKOFF).getTime() - System.currentTimeMillis();

            final String countdownToDraft = String.format("%02dd %02dh %02dm %02ds",
                    TimeUnit.MILLISECONDS.toDays(millisTilDraft),
                    TimeUnit.MILLISECONDS.toHours(millisTilDraft) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisTilDraft)),
                    TimeUnit.MILLISECONDS.toMinutes(millisTilDraft) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisTilDraft)),
                    TimeUnit.MILLISECONDS.toSeconds(millisTilDraft) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisTilDraft)));

            final String countdownToNflSeasonStart = String.format("%02dd %02dh %02dm %02ds",
                    TimeUnit.MILLISECONDS.toDays(millisTilSeasonStart),
                    TimeUnit.MILLISECONDS.toHours(millisTilSeasonStart) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisTilSeasonStart)),
                    TimeUnit.MILLISECONDS.toMinutes(millisTilSeasonStart) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisTilSeasonStart)),
                    TimeUnit.MILLISECONDS.toSeconds(millisTilSeasonStart) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisTilSeasonStart)));

            sb.append("COUNTDOWN " + Emojis.HOUR_GLASS + "\\n");
            sb.append("Draft Day: " + countdownToDraft + "\\n");
            sb.append("NFL Season: " + countdownToNflSeasonStart);

            return sb.toString();
        } catch (ParseException e) {
            return "Provided dates are not formatted correctly.";
        }
    }
}
