package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.espn.Espn;
import ncollins.model.chat.PollPayload;
import ncollins.model.espn.League;
import ncollins.model.espn.Season;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EspnTradeScheduler implements Scheduler {
    private GroupMeBot bot;
    private static final String TRADE_POLL_TITLE = "All in favor of approving this trade? \uD83D\uDC46";

    public EspnTradeScheduler(GroupMeBot bot){
        this.bot = bot;
    }

    public void start(){
        // schedule task every 5 minutes
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> checkEspnTrade(), 0, 5, TimeUnit.MINUTES);
    }

    private void checkEspnTrade(){
    }

    private void sendTradePoll(){
        //send breaking news message
        bot.sendMessage("\uD83D\uDEA8 BREAKING NEWS \uD83D\uDEA8 \\n\\n" +
                "TEAM1 and TEAM2 have agreed on a trade, multiple sauces tell " + bot.getBotName() + ". Here are the details...\\n\\n" +
                "TEAM1_ABBREV receives:\\nPLAYERS\\n" +
                "TEAM2_ABBREV receives:\\nPLAYERS");

        //send trade approval poll message that expires after 1 hour
        long expiration = (System.currentTimeMillis() + 60 * 60000)/1000; // measured in seconds
        PollPayload payload = new PollPayload(TRADE_POLL_TITLE, new String[]{"Yay","Nay"}, expiration);
        bot.sendPollMessage(payload);
    }
}