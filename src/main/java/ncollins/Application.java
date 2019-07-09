package ncollins;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.chat.groupme.GroupMeListener;
import ncollins.chat.groupme.GroupMeProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        String GROUP_ID =               System.getenv("GROUP_ME_GROUP_ID");
        String USER_ID =                System.getenv("GROUP_ME_USER_ID");
        String GROUP_ME_ACCESS_TOKEN =  System.getenv("GROUP_ME_ACCESS_TOKEN");
        String MAIN_BOT_ID =            System.getenv("MAIN_BOT_ID");
        String MAIN_BOT_NAME =          System.getenv("MAIN_BOT_NAME");
        String ESPN_BOT_ID =            System.getenv("ESPN_BOT_ID");
        String ESPN_BOT_NAME =          System.getenv("ESPN_BOT_NAME");

        SpringApplication.run(Application.class, args);

        GroupMeBot mainBot = new GroupMeBot(MAIN_BOT_ID,MAIN_BOT_NAME,GROUP_ID,USER_ID);
        GroupMeBot espnBot = new GroupMeBot(ESPN_BOT_ID,ESPN_BOT_NAME,GROUP_ID,USER_ID);
        new GroupMeListener(new GroupMeProcessor(mainBot, espnBot, GROUP_ME_ACCESS_TOKEN), GROUP_ME_ACCESS_TOKEN).listen();
    }
}
