package ncollins.chat.groupme;

import ncollins.gif.GifGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainGroupMeBot extends GroupMeBot {
    private static final String GROUP_ID = System.getenv("GROUP_ME_GROUP_ID");
    private static final String USER_ID = System.getenv("GROUP_ME_USER_ID");
    private static final String GROUP_ME_ACCESS_TOKEN = System.getenv("GROUP_ME_ACCESS_TOKEN");
    private static final String MAIN_BOT_ID = System.getenv("MAIN_BOT_ID");
    private static final String MAIN_BOT_NAME = System.getenv("MAIN_BOT_NAME");

    @Autowired
    public MainGroupMeBot(GifGenerator gifGenerator){
        super(gifGenerator, GROUP_ME_ACCESS_TOKEN, MAIN_BOT_ID, MAIN_BOT_NAME, GROUP_ID, USER_ID);
    }
}
