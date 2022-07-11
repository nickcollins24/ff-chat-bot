package ncollins.controllers;

import ncollins.chat.ChatBotProcessor;
import ncollins.data.PinCollection;
import ncollins.model.chat.Pin;
import ncollins.model.chat.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bot")
public class BotController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ChatBotProcessor processor;
    private PinCollection pinCollection;

    @Autowired
    public BotController(ChatBotProcessor processor, PinCollection pinCollection){
        this.processor = processor;
        this.pinCollection = pinCollection;
    }

    @GetMapping("pin")
    public List<Pin> getPins() {
        return pinCollection.getPins();
    }

    @PostMapping("callback")
    public void botCallback(@RequestBody Subject subject){
        logger.info("Bot Callback initiated on: " + subject.toString());
        processor.processResponse(subject, System.currentTimeMillis());
    }
}
