package ncollins.chat.processors;

import ncollins.chat.bots.groupme.EspnGroupMeBot;
import ncollins.chat.bots.groupme.MainGroupMeBot;
import ncollins.model.chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupMeProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MainGroupMeBot mainBot;
    private EspnGroupMeBot espnBot;
    private EspnProcessor espnProcessor;
    private EasterEggProcessor easterEggProcessor;
    private BotProcessor botProcessor;

    @Autowired
    public GroupMeProcessor(MainGroupMeBot mainBot,
                            EspnGroupMeBot espnBot,
                            EspnProcessor espnProcessor,
                            EasterEggProcessor easterEggProcessor,
                            BotProcessor botProcessor){
        this.mainBot = mainBot;
        this.espnBot = espnBot;
        this.espnProcessor = espnProcessor;
        this.easterEggProcessor = easterEggProcessor;
        this.botProcessor = botProcessor;
    }

    public MainGroupMeBot getMainBot(){
        return mainBot;
    }

    public void processResponse(Subject subject) {
        logger.info("Incoming message: " + subject.getText());

        //send to bot for processing if message was created by a user (not bot) in the required group
        if(subject.getGroupId().equals(mainBot.getBotGroupId()) && subject.getSenderType().equals("user")){
            String fromUser = subject.getName();
            String text = subject.getText().trim().toLowerCase();

            if(text.contains("@here"))
                mainBot.sendMessageWithMention("@here " + Emojis.EYES_LEFT + Emojis.FINGER_UP, new int[]{0,5});

            // summon bot
            if(text.startsWith(mainBot.getBotKeyword())) {
                String textNoKeyword = text.replace(mainBot.getBotKeyword(), "").trim();

                // espnbot
                if(textNoKeyword.startsWith("show ")) {
                    ProcessResult espnResult = espnProcessor.processResponse(textNoKeyword.replace("show", "").trim());
                    if(!espnResult.getText().isEmpty())
                        espnBot.sendMessage(espnResult.getText());
                // mainbot
                } else {
                    ProcessResult mainResult = botProcessor.processResponse(
                            textNoKeyword,
                            mainBot.getBotKeyword());
                    if(!mainResult.getText().isEmpty())
                        mainBot.sendMessage(mainResult.getText());
                }
            // hunt for easter eggs
            } else {
                ProcessResult easterEggResult = easterEggProcessor.processResponse(text);
                if(!easterEggResult.getText().isEmpty())
                    mainBot.sendMessage(easterEggResult.getText());
            }
        } else {
            logger.info("Skipping bot message: " + subject.getText());
        }
    }
}
