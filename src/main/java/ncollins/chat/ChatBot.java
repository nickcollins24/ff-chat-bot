package ncollins.chat;

import ncollins.model.chat.ChatResponse;

public interface ChatBot {
    void sendMessage(String text, String[] imageUrl);
    void processResponse(ChatResponse response);
}
