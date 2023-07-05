package ncollins.chat.processors;

import ncollins.gif.GifGenerator;
import ncollins.hug.HugGenerator;
import ncollins.magiceightball.MagicAnswerGenerator;
import ncollins.model.chat.Emojis;
import ncollins.model.chat.ProcessResult;
import ncollins.model.chat.ProcessResultType;
import ncollins.salt.SaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Component
public class BotProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String DRAFT_DAY = System.getenv("DRAFT_DAY");
    private static final String NFL_KICKOFF = System.getenv("NFL_KICKOFF");

    private SaltGenerator saltGenerator;
    private HugGenerator hugGenerator;
    private MagicAnswerGenerator answerGenerator;
    private GifGenerator gifGenerator;

    @Autowired
    public BotProcessor(SaltGenerator saltGenerator,
                            HugGenerator hugGenerator,
                            MagicAnswerGenerator answerGenerator,
                            GifGenerator gifGenerator){
        this.saltGenerator = saltGenerator;
        this.hugGenerator = hugGenerator;
        this.answerGenerator = answerGenerator;
        this.gifGenerator = gifGenerator;
    }

    public ProcessResult processResponse(String text, String botKeyword){
        if(text.matches("^$"))
            return new ProcessResult(ProcessResultType.TEXT, buildHelpMessage(botKeyword));
        else if(text.matches("^help$"))
            return new ProcessResult(ProcessResultType.TEXT, buildShowCommandsMessage(botKeyword));
        else if(text.startsWith("gif "))
            return new ProcessResult(ProcessResultType.IMAGE, gifGenerator.search(text.replace("gif","").trim()));
        else if(text.startsWith("salt "))
            return new ProcessResult(ProcessResultType.TEXT, buildSaltMessage(text.replace("salt","").trim()));
        else if(text.startsWith("hug "))
            return new ProcessResult(ProcessResultType.TEXT, buildHugMessage(text.replace("hug","").trim()));
        else if(text.equals("countdown"))
            return new ProcessResult(ProcessResultType.TEXT, buildCountdown());
        else if(text.endsWith("?"))
            return new ProcessResult(ProcessResultType.TEXT, buildMagicAnswerMessage());
        else return new ProcessResult(ProcessResultType.TEXT, "");
    }

    private String buildHelpMessage(String botKeyword){
        return "you rang? type '" + botKeyword + " help' to see what i can do.";
    }

    private String buildShowCommandsMessage(String botKeyword){
        return "commands:\\n" +
                botKeyword + " help -- show bot commands\\n" +
                botKeyword + " gif [SOMETHING] -- post a random gif of something\\n" +
                botKeyword + " salt [SOMEONE] -- throw salt at someone\\n" +
                botKeyword + " hug [SOMEONE] -- give someone a hug\\n" +
                botKeyword + " [QUESTION]? -- ask a yes/no question\\n" +
                botKeyword + " show {top|bottom} [TOTAL] scores {ever|YEAR|} -- top/bottom scores\\n" +
                botKeyword + " show {top|bottom} [TOTAL] records {ever|YEAR|} -- top/bottom records\\n" +
                botKeyword + " show {top|bottom} [TOTAL] pf through [WEEK] -- top/bottom pf through given week\\n" +
                botKeyword + " show {top|bottom} [TOTAL] {wins/losses} through [WEEK] -- top/bottom w/l through given week\\n" +
                botKeyword + " show {top|bottom} [TOTAL] {POSITION|players} {WEEK|} -- best/worst players\\n" +
                botKeyword + " show [TOTAL] blowouts -- biggest wins\\n" +
                botKeyword + " show [TOTAL] heartbreaks -- closest losses\\n" +
                botKeyword + " show standings {ever|YEAR|} -- standings\\n" +
                botKeyword + " show playoff standings {ever|} -- playoff standings\\n" +
                botKeyword + " show matchups [TEAM1] [TEAM2] -- matchup stats between two teams\\n" +
                botKeyword + " show pf winners -- list of most pf per week\\n" +
                botKeyword + " show trades {YEAR|} -- list of trades\\n" +
                botKeyword + " show jujus -- all time jujus\\n" +
                botKeyword + " show salties -- all time salties\\n" +
                botKeyword + " show champs -- all time champions\\n" +
                botKeyword + " show sackos -- all time sackos";
    }

    private String buildSaltMessage(String recipient) {
        return saltGenerator.throwSalt(recipient);
    }

    private String buildHugMessage(String recipient) {
        return hugGenerator.giveHug(recipient);
    }

    private String buildMagicAnswerMessage(){
        return answerGenerator.getRandom();
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
