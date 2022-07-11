package ncollins;

import ncollins.chat.groupme.*;
import ncollins.data.PinCollection;
import ncollins.espn.Espn;
import ncollins.espn.EspnDataLoader;
import ncollins.espn.EspnHttpClient;
import ncollins.espn.EspnMessageBuilder;
import ncollins.gif.GifGenerator;
import ncollins.gif.GiphyGenerator;
import ncollins.helpers.StringHelpers;
import ncollins.magiceightball.MagicAnswerGenerator;
import ncollins.salt.SaltGenerator;
import ncollins.schedulers.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        String CHAT_LISTENER_ENABLED =          System.getenv("CHAT_LISTENER_ENABLED");
        String MUNNDAY_SCHEDULER_ENABLED =      System.getenv("MUNNDAY_SCHEDULER_ENABLED");
        String ROUNDUP_SCHEDULER_ENABLED =      System.getenv("ROUNDUP_SCHEDULER_ENABLED");
        String REMINDER_SCHEDULER_ENABLED =     System.getenv("REMINDER_SCHEDULER_ENABLED");
        String GAMEDAY_SCHEDULER_ENABLED =      System.getenv("GAMEDAY_SCHEDULER_ENABLED");
        String TRANSACTION_SCHEDULER_ENABLED =  System.getenv("TRANSACTION_SCHEDULER_ENABLED");

        SpringApplication.run(Application.class, args);

        // init message processor and bots
        GifGenerator gifGenerator = new GiphyGenerator();
        MainGroupMeBot mainBot = new MainGroupMeBot(gifGenerator);

        Espn espn = new Espn(new EspnDataLoader(new EspnHttpClient()));
        EspnMessageBuilder espnMessageBuilder = new EspnMessageBuilder(espn);
        EspnGroupMeBot espnBot = new EspnGroupMeBot(espnMessageBuilder, gifGenerator);

        // start listening for group me messages
        if(StringHelpers.isTrue(CHAT_LISTENER_ENABLED)) {
            GroupMeProcessor processor = new GroupMeProcessor(
                    mainBot,
                    espnBot,
                    new SaltGenerator(),
                    new MagicAnswerGenerator(),
                    new PinCollection());
            new GroupMeListener(processor).listen();
        }

        // start schedulers
        if(StringHelpers.isTrue(MUNNDAY_SCHEDULER_ENABLED)) new MunndayScheduler(mainBot, gifGenerator).start();               // Monday
        if(StringHelpers.isTrue(ROUNDUP_SCHEDULER_ENABLED)) new WeeklyRoundupScheduler(mainBot, espnMessageBuilder).start();   // Tuesday
        if(StringHelpers.isTrue(REMINDER_SCHEDULER_ENABLED)) new LineupReminderScheduler(mainBot).start();                     // Thursday
        if(StringHelpers.isTrue(GAMEDAY_SCHEDULER_ENABLED)) new GameDayScheduler(mainBot, espn).start();                       // Sunday
        if(StringHelpers.isTrue(TRANSACTION_SCHEDULER_ENABLED)) new EspnTransactionScheduler(espnBot, espn).start();
    }
}
