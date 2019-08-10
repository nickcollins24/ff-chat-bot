package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.model.chat.PollPayload;

public class TradeNotificationScheduler implements Scheduler {
    private GroupMeBot bot;
    private static final String TRADE_POLL_TITLE = "All in favor of approving this trade? \uD83D\uDC46";

    public TradeNotificationScheduler(GroupMeBot bot){
        this.bot = bot;
    }

    public void start(){
        //send breaking news message
        bot.sendMessage("https://i.groupme.com/500x281.jpeg.87bd736636764acf86e1bd131a6f9373");
        bot.sendMessage("TEAM1 and TEAM2 have agreed on a trade, multiple sauces tell " + bot.getBotName() + ". Here are the details...\\n\\n" +
                                "TEAM1_ABBREV receives:\\nPLAYERS\\n" +
                                "TEAM2_ABBREV receives:\\nPLAYERS");

        //send trade approval poll message that expires after 1 hour
        long expiration = (System.currentTimeMillis() + 60 * 60000)/1000; // measured in seconds
        PollPayload payload = new PollPayload(TRADE_POLL_TITLE, new String[]{"Yay","Nay"}, expiration);
        bot.sendPollMessage(payload);
    }
}
