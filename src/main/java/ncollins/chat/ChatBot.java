package ncollins.chat;

import ncollins.model.chat.ImagePayload;
import ncollins.model.chat.MentionPayload;

public interface ChatBot {
    void sendMessage(String text, ImagePayload images);
    void sendMessage(String text, MentionPayload images);
}
