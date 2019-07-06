package ncollins.chat;

public interface ChatBotProcessor {
    void processResponse(String fromUser, String text, String[] imageUrl);
}
