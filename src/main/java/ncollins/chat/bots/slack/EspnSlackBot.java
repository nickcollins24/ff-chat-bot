package ncollins.chat.bots.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EspnSlackBot extends SlackBot {
    private static final String SLACK_ESPN_AUTH_TOKEN = System.getenv("SLACK_ESPN_AUTH_TOKEN");
    private static final String ESPN_BOT_ID = System.getenv("SLACK_ESPN_BOT_ID");
    private static final String ESPN_BOT_NAME = System.getenv("ESPN_BOT_NAME");

    @Autowired
    public EspnSlackBot(){
        super(SLACK_ESPN_AUTH_TOKEN, ESPN_BOT_ID, ESPN_BOT_NAME);
    }
}
