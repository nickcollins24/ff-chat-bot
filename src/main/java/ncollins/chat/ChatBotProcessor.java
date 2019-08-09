package ncollins.chat;

import ncollins.model.chat.ChatResponse;

public interface ChatBotProcessor {
    void processResponse(String fromUser, String text, ChatResponse.Attachment[] attachments, long currentTime);
}
