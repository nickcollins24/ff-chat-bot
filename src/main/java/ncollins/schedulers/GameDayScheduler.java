package ncollins.schedulers;

import ncollins.chat.bots.groupme.MainGroupMeBot;
import ncollins.espn.EspnMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled every Sunday @ 10AM PST
 */
@Component
@ConditionalOnProperty(value = "GAMEDAY_SCHEDULER_ENABLED",
                       havingValue = "true")
public class GameDayScheduler implements Scheduler {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MainGroupMeBot bot;
    private EspnMessageBuilder espnMessageBuilder;

    public GameDayScheduler(MainGroupMeBot bot, EspnMessageBuilder espnMessageBuilder){
        this.bot = bot;
        this.espnMessageBuilder = espnMessageBuilder;
        start();
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Sunday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(10,0), ChronoUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> sendGameDayMessage(), startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);

        logger.info("GameDayScheduler enabled.");
    }

    private void sendGameDayMessage(){
        bot.sendMessage(espnMessageBuilder.buildGamedayMessage());
    }
}
