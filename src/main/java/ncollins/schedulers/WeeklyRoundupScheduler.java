package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.espn.Espn;
import ncollins.espn.EspnMessageBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeeklyRoundupScheduler implements Scheduler {
    private GroupMeBot bot;
    private EspnMessageBuilder espnMessageBuilder;

    public WeeklyRoundupScheduler(GroupMeBot bot, EspnMessageBuilder espnMessageBuilder){
        this.bot = bot;
        this.espnMessageBuilder = espnMessageBuilder;
    }

    public void start(){
        Runnable task = () -> bot.sendMessage(espnMessageBuilder.buildWeeklyRoundupMessage());
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Tuesday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)).atTime(10,0), ChronoUnit.MINUTES);
        scheduler.scheduleAtFixedRate(task, startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);
    }
}
