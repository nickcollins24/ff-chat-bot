package ncollins.controllers;

import ncollins.chat.processors.GroupMeProcessor;
import ncollins.chat.processors.SlackProcessor;
import ncollins.model.chat.slack.SlackEventPayload;
import ncollins.model.chat.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bot")
public class BotController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private GroupMeProcessor groupMeProcessor;
    private SlackProcessor slackProcessor;

    @Autowired
    public BotController(GroupMeProcessor groupMeProcessor, SlackProcessor slackProcessor){
        this.groupMeProcessor = groupMeProcessor;
        this.slackProcessor = slackProcessor;
    }

    @PostMapping("callback")
    public void botCallback(@RequestBody Subject subject){
        logger.info("Bot Callback initiated on: " + subject.toString());
        groupMeProcessor.processResponse(subject);
    }

    /**
     *    Note: currently only supports the following events-
     *      * message.channels (A message was posted to a channel)
     *      * message.groups (A message was posted to a private channel)
     */
    @PostMapping("slack/callback")
    public String slackBotCallback(@RequestBody SlackEventPayload slackEventPayload){
        logger.info("SlackBot Event Callback initiated on: " + slackEventPayload.toString());

        // incoming slack validation request
        if(slackEventPayload.getChallenge() != null){
            return slackEventPayload.getChallenge();
        // incoming slack event
        } else {
            slackProcessor.processResponse(slackEventPayload);
            return "ok";
        }
    }
}
