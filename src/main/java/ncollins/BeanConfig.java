package ncollins;

import ncollins.chat.bots.Bot;
import ncollins.chat.bots.groupme.EspnGroupMeBot;
import ncollins.chat.bots.groupme.MainGroupMeBot;
import ncollins.chat.bots.slack.EspnSlackBot;
import ncollins.chat.bots.slack.MainSlackBot;
import ncollins.model.chat.PlatformType;
import ncollins.schedulers.EspnTransactionScheduler;
import ncollins.schedulers.GameDayScheduler;
import ncollins.schedulers.MunndayScheduler;
import ncollins.schedulers.WeeklyRoundupScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    private static final String PLATFORM = System.getenv("PLATFORM");
    private static final String ESPN_TRANSACTION_CHAT_ID = System.getenv("SLACK_ESPN_TRANSACTION_CHAT_ID");
    private static final String GAMEDAY_CHAT_ID = System.getenv("SLACK_GAMEDAY_CHAT_ID");
    private static final String MUNNDAY_CHAT_ID = System.getenv("SLACK_MUNNDAY_CHAT_ID");
    private static final String ROUNDUP_CHAT_ID = System.getenv("SLACK_ROUNDUP_CHAT_ID");

    @Bean
    @ConditionalOnProperty(value = "TRANSACTION_SCHEDULER_ENABLED",
            havingValue = "true")
    public EspnTransactionScheduler getEspnTransactionSchedulerBean() {
        switch(PlatformType.valueOf(PLATFORM.toUpperCase())){
            case GROUPME: {
                Bot espnGroupMeBot = new EspnGroupMeBot();
                return new EspnTransactionScheduler(espnGroupMeBot);
            }
            // default to Slack
            default: {
                Bot espnSlackBot = new EspnSlackBot();
                espnSlackBot.setChatId(ESPN_TRANSACTION_CHAT_ID);
                return new EspnTransactionScheduler(espnSlackBot);
            }
        }
    }

    @Bean
    @ConditionalOnProperty(value = "GAMEDAY_SCHEDULER_ENABLED",
            havingValue = "true")
    public GameDayScheduler getGameDaySchedulerBean() {
        switch(PlatformType.valueOf(PLATFORM.toUpperCase())){
            case GROUPME: {
                Bot mainGroupMeBot = new MainGroupMeBot();
                return new GameDayScheduler(mainGroupMeBot);
            }
            // default to Slack
            default: {
                Bot mainSlackBot = new MainSlackBot();
                mainSlackBot.setChatId(GAMEDAY_CHAT_ID);
                return new GameDayScheduler(mainSlackBot);
            }
        }
    }

    @Bean
    @ConditionalOnProperty(value = "MUNNDAY_SCHEDULER_ENABLED",
            havingValue = "true")
    public MunndayScheduler getMunndaySchedulerBean() {
        switch(PlatformType.valueOf(PLATFORM.toUpperCase())){
            case GROUPME: {
                Bot mainGroupMeBot = new MainGroupMeBot();
                return new MunndayScheduler(mainGroupMeBot);
            }
            // default to Slack
            default: {
                Bot mainSlackBot = new MainSlackBot();
                mainSlackBot.setChatId(MUNNDAY_CHAT_ID);
                return new MunndayScheduler(mainSlackBot);
            }
        }
    }

    @Bean
    @ConditionalOnProperty(value = "ROUNDUP_SCHEDULER_ENABLED",
            havingValue = "true")
    public WeeklyRoundupScheduler getWeeklyRoundupSchedulerBean() {
        switch(PlatformType.valueOf(PLATFORM.toUpperCase())){
            case GROUPME: {
                Bot mainGroupMeBot = new MainGroupMeBot();
                return new WeeklyRoundupScheduler(mainGroupMeBot);
            }
            // default to Slack
            default: {
                Bot mainSlackBot = new MainSlackBot();
                mainSlackBot.setChatId(ROUNDUP_CHAT_ID);
                return new WeeklyRoundupScheduler(mainSlackBot);
            }
        }
    }
}