package ncollins.chat.bots;

import java.util.ArrayList;
import java.util.List;

public abstract class Bot {
    private String authToken;
    private String botId;
    private String botName;
    private String botKeyword;
    private String botMention;
    private String chatId;

    public Bot(String authToken,
               String botId,
               String botName,
               String chatId){
        this.authToken = authToken;
        this.botId = botId;
        this.botName = botName;
        this.botKeyword = "@" + botName;
        this.botMention = this.botKeyword;
        this.chatId = chatId;
    }

    public Bot(String authToken,
                    String botId,
                    String botName){
        this(authToken, botId, botName, "");
    }

    public String getBotId() {
        return botId;
    }

    public String getBotKeyword() {
        return botKeyword;
    }

    public String getBotMention(){
        return botMention;
    }

    public void setBotMention(String botMention){ this.botMention  = botMention; }

    public String getBotName(){
        return botName;
    }

    public String getAuthToken() { return authToken; }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) { this.chatId = chatId; }

    public abstract void sendMessage(String text);

    public abstract void sendImage(String imageUrl);

    /**
     * Splits message into chunks, ensuring that no chunk exceeds MAX_MESSAGE_LENGTH characters.
     * This is to avoid exceeding GroupMe's character limit, which results in a failed send.
     * @param text
     * @return
     */
    protected List<String> splitMessage(String text, int maxLength){
        List<String> messages = new ArrayList();

        if(text.toCharArray().length <= maxLength){
            messages.add(text);
            return messages;
        }

        while(text.toCharArray().length > maxLength){
            int index = text.lastIndexOf("\\n", maxLength);
            messages.add(text.substring(0, index+2));
            text = text.substring(index+2);
        }
        messages.add(text);

        return messages;
    }
}
