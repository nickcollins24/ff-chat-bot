package ncollins.helpers;

import ncollins.chat.bots.Bot;
import ncollins.chat.bots.groupme.EspnGroupMeBot;
import ncollins.chat.bots.groupme.MainGroupMeBot;
import ncollins.chat.bots.slack.EspnSlackBot;
import ncollins.chat.bots.slack.MainSlackBot;
import ncollins.model.chat.BotType;
import ncollins.model.chat.PlatformType;

public class BotFactory {
    private static final String PLATFORM = System.getenv("PLATFORM");

    public static Bot buildBot(BotType botType, String chatId){
        PlatformType platformType = PlatformType.valueOf(PLATFORM.toUpperCase());

        if(PlatformType.SLACK.equals(platformType) && BotType.MAIN.equals(botType)){
            Bot mainSlackBot = new MainSlackBot();
            mainSlackBot.setChatId(chatId);
            return mainSlackBot;
        } else if(PlatformType.SLACK.equals(platformType) && BotType.ESPN.equals(botType)){
            Bot espnSlackBot = new EspnSlackBot();
            espnSlackBot.setChatId(chatId);
            return espnSlackBot;
        } else if(PlatformType.GROUPME.equals(platformType) && BotType.MAIN.equals(botType)){
            return new MainGroupMeBot();
        // default
        } else return new EspnGroupMeBot();
    }
}
