package ncollins.schedulers;

import ncollins.chat.bots.Bot;
import ncollins.gif.GifGenerator;
import ncollins.helpers.BotFactory;
import ncollins.model.chat.BotType;
import ncollins.model.chat.Emojis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled every Monday @ 10AM PST
 */
@Component
@ConditionalOnProperty(value = "MUNNDAY_SCHEDULER_ENABLED",
                       havingValue = "true")
public class MunndayScheduler implements Scheduler {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String MUNNDAY_CHAT_ID = System.getenv("SLACK_MUNNDAY_CHAT_ID");

    private Bot bot;
    private GifGenerator gifGenerator;

    public MunndayScheduler(GifGenerator gifGenerator){
        this.bot = BotFactory.buildBot(BotType.MAIN, MUNNDAY_CHAT_ID);
        this.gifGenerator = gifGenerator;
        start();
    }

    public void start() {
        Runnable task = () -> {
            bot.sendImage("Happy Munnday " + Emojis.PARTY_SMILEY,
                    gifGenerator.search("olivia munn"));
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Monday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)).atTime(10,0), ChronoUnit.MINUTES);

        scheduler.scheduleAtFixedRate(task, startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);

        logger.info("MunndayScheduler enabled.");
    }
}
