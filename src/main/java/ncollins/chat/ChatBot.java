package ncollins.chat;

public interface ChatBot {
    void sendMessage(String text, String[] imageUrl);
    void processResponse(String fromUser, String text, String[] imageUrl);
}
