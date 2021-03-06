package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LineupReminderScheduler implements Scheduler {
    private GroupMeBot bot;

    public LineupReminderScheduler(GroupMeBot bot){
        this.bot = bot;
    }

    public void start(){
        Runnable task = () -> bot.sendMessageWithMention("@here friendly reminder to set your lineup!", new int[]{0,5});

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Thursday at 12PM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY)).atTime(10,0), ChronoUnit.MINUTES);
        scheduler.scheduleAtFixedRate(task, startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);
    }
}
