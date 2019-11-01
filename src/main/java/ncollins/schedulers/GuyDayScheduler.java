package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.gif.GifGenerator;
import ncollins.model.chat.Emojis;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GuyDayScheduler implements Scheduler {
    private GifGenerator gifGenerator = new GifGenerator();
    private GroupMeBot bot;
    private List<String> guys = new ArrayList();

    public GuyDayScheduler(GroupMeBot bot){
        this.bot = bot;
        this.guys.add("adam levine");
        this.guys.add("justin timberlake");
        this.guys.add("vin diesel");
    }

    public void start() {
        Runnable task = () -> {
            bot.sendMessage("Happy Lady Appreciation Day! " + Emojis.PARTY_SMILEY);
            bot.sendMessage(gifGenerator.searchGif(
                    guys.get(ThreadLocalRandom.current().nextInt(0, guys.size())) + " sexy"));
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Friday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).atTime(10,0), ChronoUnit.MINUTES);

        scheduler.scheduleAtFixedRate(task, startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);
    }
}
