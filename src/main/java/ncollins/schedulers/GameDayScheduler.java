package ncollins.schedulers;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.espn.Espn;
import ncollins.model.chat.Emojis;
import ncollins.model.espn.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameDayScheduler implements Scheduler {
    private GroupMeBot bot;
    private Espn espn;

    public GameDayScheduler(GroupMeBot bot, Espn espn){
        this.bot = bot;
        this.espn = espn;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // schedule task every Sunday at 10AM PST
        Long startTime = LocalDateTime.now(TimeZone.getTimeZone("PST").toZoneId()).until(
                LocalDate.now(TimeZone.getTimeZone("PST").toZoneId()).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(10,0), ChronoUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> sendGameDayMessage(), startTime, TimeUnit.DAYS.toMinutes(7), TimeUnit.MINUTES);
    }

    private void sendGameDayMessage(){
        List<Matchup> matchups = espn.getMatchups(espn.getCurrentScoringPeriodId(), espn.getCurrentSeasonId());
        StringBuilder sb = new StringBuilder();

        sb.append("Its GameDay! Heres whats on tap " + Emojis.BEER_MUG + " " + Emojis.FOOTBALL + "\\n\\n");
        for(Matchup matchup : matchups){
            Member m0 = espn.getMemberByTeamId(matchup.getScheduleItem().getHome().getTeamId());
            Member m1 = espn.getMemberByTeamId(matchup.getScheduleItem().getAway().getTeamId());
            Map<Member, Record> recordBetween = espn.getRecordBetween(m0,m1);

            sb.append(m0.getFirtName() + " " + m0.getLastName() + " (" + recordBetween.get(m0).getOverall().getWins() + ") vs. " +
                      m1.getFirtName() + " " + m1.getLastName() + " (" + recordBetween.get(m1).getOverall().getWins() + ")\\n");
        }
        sb.append("\\n(x) = career wins vs. opponent");

        bot.sendMessage(sb.toString());
    }
}
