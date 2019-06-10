package ncollins;

import ncollins.chat.ChatBotListener;
import ncollins.chat.groupme.GroupMeBot;
import ncollins.chat.groupme.GroupMeListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        final String BOT_ID = "7f608eb2e1c2b036fe461a4765";
        final String BOT_NAME = "testbot";

        ChatBotListener chatListener = new GroupMeListener(new GroupMeBot(BOT_ID,BOT_NAME));
        chatListener.listen();
    }
}
