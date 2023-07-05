package ncollins.chat.bots.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainSlackBot extends SlackBot {
    private static final String SLACK_MAIN_AUTH_TOKEN = System.getenv("SLACK_MAIN_AUTH_TOKEN");
    private static final String MAIN_BOT_ID = System.getenv("SLACK_MAIN_BOT_ID");
    private static final String MAIN_BOT_NAME = System.getenv("MAIN_BOT_NAME");

    @Autowired
    public MainSlackBot(){
        super(SLACK_MAIN_AUTH_TOKEN, MAIN_BOT_ID, MAIN_BOT_NAME);
    }
}
