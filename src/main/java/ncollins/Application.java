package ncollins;

import ncollins.chat.groupme.GroupMeBot;
import ncollins.chat.groupme.GroupMeListener;
import ncollins.chat.groupme.GroupMeProcessor;
import ncollins.data.PinCollection;
import ncollins.espn.Espn;
import ncollins.schedulers.LineupReminderScheduler;
import ncollins.schedulers.MunndayScheduler;
import ncollins.schedulers.EspnRefreshScheduler;
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
        // these only get set when testing locally, dont set these if running in GCP
        String GCP_PROJECT_ID =         System.getenv("GCP_PROJECT_ID");
        String GCP_KEY =                System.getenv("GCP_KEY");

        SpringApplication.run(Application.class, args);

        // init bots
        GroupMeBot mainBot = new GroupMeBot(GROUP_ME_ACCESS_TOKEN, MAIN_BOT_ID, MAIN_BOT_NAME, GROUP_ID, USER_ID);
        GroupMeBot espnBot = new GroupMeBot(GROUP_ME_ACCESS_TOKEN, ESPN_BOT_ID, ESPN_BOT_NAME, GROUP_ID, USER_ID);
        PinCollection pinCollection = new PinCollection(GCP_PROJECT_ID, GCP_KEY);
        Espn espn = new Espn();
        GroupMeProcessor processor = new GroupMeProcessor(mainBot, espnBot, pinCollection, espn);

        // start listening for group me messages
        new GroupMeListener(processor, GROUP_ME_ACCESS_TOKEN).listen();

        // start schedulers
        new MunndayScheduler(mainBot).start();
        new LineupReminderScheduler(mainBot).start();
        new EspnRefreshScheduler(espn, espnBot).start();
    }
}
