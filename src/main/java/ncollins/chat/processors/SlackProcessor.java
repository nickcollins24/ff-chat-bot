package ncollins.chat.processors;

import ncollins.chat.bots.slack.EspnSlackBot;
import ncollins.chat.bots.slack.MainSlackBot;
import ncollins.clients.SlackHttpClient;
import ncollins.hug.HugGenerator;
import ncollins.model.chat.slack.*;
import org.apache.commons.lang3.StringUtils;
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
    private HugGenerator hugGenerator;
    private SlackHttpClient slackClient;

    @Autowired
    public SlackProcessor(MainSlackBot mainBot,
                          EspnSlackBot espnBot,
                          EspnProcessor espnProcessor,
                          EasterEggProcessor easterEggProcessor,
                          BotProcessor botProcessor,
                          HugGenerator hugGenerator,
                          SlackHttpClient slackClient){
        this.mainBot = mainBot;
        this.espnBot = espnBot;
        this.espnProcessor = espnProcessor;
        this.easterEggProcessor = easterEggProcessor;
        this.botProcessor = botProcessor;
        this.hugGenerator = hugGenerator;
        this.slackClient = slackClient;
    }

    public void processResponse(SlackEventPayload payload) {
        logger.info("Incoming slack payload: " + payload);

        try {
            switch(EventType.valueOf(payload.getEvent().getType().toUpperCase())){
                case REACTION_ADDED: {
                    processReaction(payload);
                    break;
                }
                case MESSAGE: {
                    processMessage(payload);
                    break;
                }
                default: logger.info("Skipping slack payload: " + payload.getEventId());
            }
        } catch(IllegalArgumentException e){
            logger.info("Slack event type not recognized. Skipping payload: " + payload.getEventId());
        }
    }

    private void processReaction(SlackEventPayload payload){
        Event event = payload.getEvent();
        String reaction = event.getReaction();
        String user = event.getUser();
        String toUser = event.getItemUser();

        // reaction and user must be available
        if(reaction == null || user == null) return;

        /**
         * send to bot for processing if reaction was:
         * 1. added by a user (not bot)
         * 2. added to a message with text (not bot's)
         */
        if(event.getItem().getType().equals("message") &&
                !mainBot.getBotId().equals(user) &&
                !mainBot.getBotId().equals(toUser) &&
                !espnBot.getBotId().equals(user) &&
                !espnBot.getBotId().equals(toUser)){

            try {
                ReactionType reactionType = ReactionType.valueOf(reaction.toUpperCase());

                switch(reactionType){
                    case MOCK:
                    case SPONGEBOB_MOCK: {
                        mainBot.mockMessage(
                                event.getItem().getTs(), event.getItem().getChannel(), event.getItem().getTs());
                        break;
                    }
                    case HUGS: {
                        SlackUser userToHug = slackClient.getUser(toUser);

                        if(userToHug != null){
                            String userName = StringUtils.isEmpty(userToHug.getProfile().getDisplayName()) ?
                                    userToHug.getProfile().getFirstName() :
                                    userToHug.getProfile().getDisplayName();

                            mainBot.sendMessage(
                                    hugGenerator.giveHug(userName) + " :" + ReactionType.HUGS.toString().toLowerCase() + ":",
                                    event.getItem().getChannel(), event.getItem().getTs());
                        }
                        break;
                    }
                    default: logger.info("Skipping slack reaction: " + reactionType);
                }
            } catch(IllegalArgumentException e){
                logger.info("Slack reaction not recognized. Skipping reaction: " + reaction);
            }
        }
    }

    private void processMessage(SlackEventPayload payload){
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
