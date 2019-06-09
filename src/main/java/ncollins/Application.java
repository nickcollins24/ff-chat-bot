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

        ChatBotListener chatListener = new GroupMeListener(new GroupMeBot());
        chatListener.listen();
    }
}
