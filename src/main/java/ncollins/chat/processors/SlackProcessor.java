package ncollins.chat.processors;

import ncollins.chat.bots.slack.EspnSlackBot;
import ncollins.chat.bots.slack.MainSlackBot;
import ncollins.model.chat.slack.SlackEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlackProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MainSlackBot mainBot;
    private EspnSlackBot espnBot;
    private EspnProcessor espnProcessor;
    private EasterEggProcessor easterEggProcessor;
    private BotProcessor botProcessor;

    @Autowired
    public SlackProcessor(MainSlackBot mainBot,
                          EspnSlackBot espnBot,
                          EspnProcessor espnProcessor,
                          EasterEggProcessor easterEggProcessor,
                          BotProcessor botProcessor){
        this.mainBot = mainBot;
        this.espnBot = espnBot;
        this.espnProcessor = espnProcessor;
        this.easterEggProcessor = easterEggProcessor;
        this.botProcessor = botProcessor;
    }

    public void processResponse(SlackEventPayload payload) {
        logger.info("Incoming message: " + payload.getEvent().getText());

        // send to bot for processing if message was created by a user (not bot)
        if(payload.getEvent().getUser() != null &&
                payload.getEvent().getText() != null &&
                !payload.getEvent().getUser().equals(mainBot.getBotId()) &&
                !payload.getEvent().getUser().equals(espnBot.getBotId())){
            String text = payload.getEvent().getText().trim().toLowerCase();
            String channelId = payload.getEvent().getChannel();
            String threadId = payload.getEvent().getThreadTs();

            // summon bot
            if(text.startsWith(mainBot.getBotMention().toLowerCase())) {
                logger.info("text: " + text);
                String textNoKeyword = text.replace(mainBot.getBotMention().toLowerCase(), "").trim();

                // espnbot
                if(textNoKeyword.startsWith("show ")) {
                    logger.info("processing espn request...");
                    espnBot.sendMessage(
                            espnProcessor.processResponse(textNoKeyword.replace("show", "").trim()),
                            channelId,
                            threadId);
                // mainbot
                } else {
                    logger.info("processing main bot request...");
                    mainBot.sendMessage(
                            botProcessor.processResponse(textNoKeyword, mainBot.getBotKeyword()),
                            channelId,
                            threadId);
                }
                // hunt for easter eggs
            } else {
                logger.info("looking for possible easter eggs...");
                mainBot.sendMessage(easterEggProcessor.processResponse(text), channelId, threadId);
            }
        } else {
            logger.info("Skipping bot message: " + payload.getEvent().getText());
        }
    }
}
