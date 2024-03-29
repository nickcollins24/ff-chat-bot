package ncollins.schedulers;

import ncollins.chat.bots.Bot;
import ncollins.espn.EspnMessageBuilder;
import ncollins.helpers.BotFactory;
import ncollins.model.chat.BotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Scheduled every Tuesday @ 10AM PST
 */
@Component
@ConditionalOnProperty(value = "ROUNDUP_SCHEDULER_ENABLED",
                       havingValue = "true")
public class WeeklyRoundupScheduler implements Scheduler {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ROUNDUP_CHAT_ID = System.getenv("SLACK_ROUNDUP_CHAT_ID");

    private Bot bot;
    @Autowired
    private EspnMessageBuilder espnMessageBuilder;

    public WeeklyRoundupScheduler(EspnMessageBuilder espnMessageBuilder){
        this.bot = BotFactory.buildBot(BotType.ESPN, ROUNDUP_CHAT_ID);
        this.espnMessageBuilder = espnMessageBuilder;
        start();
    }

    public void start(){
        Runnable task = () -> bot.sendMessage(espnMessageBuilder.buildWeeklyRoundupMessage());
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Tuesday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)).atTime(10,0), ChronoUnit.MINUTES);
        scheduler.scheduleAtFixedRate(task, startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);

        logger.info("WeeklyRoundupScheduler enabled.");
    }
}
