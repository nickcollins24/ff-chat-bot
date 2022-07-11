package ncollins.chat;

import ncollins.model.chat.Subject;

public interface ChatBotProcessor {
    void processResponse(Subject subject, long currentTime);
}
