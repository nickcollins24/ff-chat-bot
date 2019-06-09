package ncollins.chat.groupme;

import ncollins.chat.ChatBot;
import ncollins.model.chat.ChatResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroupMeBot implements ChatBot {
    private HttpClient client;
    private static final String GROUP_ME_URL = "https://api.groupme.com/v3/bots/post";
    private static final String BOT_ID = "7f608eb2e1c2b036fe461a4765";

    public GroupMeBot(){
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public void processResponse(ChatResponse response) {
        sendMessage(response.getData().getSubject().getText(),
                response.getData().getSubject().getAttachments());
    }

    @Override
    public void sendMessage(String text, String[] imageUrls) {
        String attachments = buildAttachmentsPayload(imageUrls);
        String payload = "{" +
                "\"bot_id\": \"" + BOT_ID + "\"," +
                "\"text\": \"" + text + "\"," +
                "\"attachments\": " + attachments + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROUP_ME_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String buildAttachmentsPayload(String[] imageUrls){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String imageUrl : imageUrls){
            sb.append("{\"type\": \"image\",\"url\": \"" + imageUrl + "\"}");
        }
        sb.append("]");

        return sb.toString();
    }
}
