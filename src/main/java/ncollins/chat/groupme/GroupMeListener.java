package ncollins.chat.groupme;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import ncollins.chat.ChatBotListener;
import ncollins.model.chat.ChatResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;

public class GroupMeListener implements ChatBotListener {
    private GroupMeBot bot;
    private HttpClient client;
    private Gson gson;
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String WSS_PROTOCOL = "wss://";
    private static final String GROUP_ME_URL = "push.groupme.com/faye";
    private static final String GROUP_ID = "43518373";
    private static final String USER_ID = "27277860";
    private static final String ACCESS_TOKEN = "QXpw652wlQUrMRUvWO6YHk57EP2dSPzqlS5biTwe";

    public GroupMeListener(GroupMeBot bot){
        this.bot = bot;
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public void listen() {
        String clientId = handshake();
        subscribeUser(clientId);
        subscribeGroup(clientId);
        openWebSocket(clientId);
    }

    private String handshake(){
        String payload = "{\"channel\": \"/meta/handshake\"," +
                "\"version\": \"1.0\"," +
                "\"supportedConnectionTypes\": [\"websocket\"]," +
                "\"id\": \"1\"}";

        HttpResponse<String> response = buildGroupMeHttpRequestAndSend(payload);
        JsonArray jsonArray = new JsonParser().parse(response.body()).getAsJsonArray();
        return jsonArray.get(0).getAsJsonObject().get("clientId").getAsString();
    }

    private void subscribeUser(String clientId){
        String payload = "{\"channel\": \"/meta/subscribe\"," +
                "\"clientId\": \"" + clientId + "\"," +
                "\"subscription\": \"/user/" + USER_ID + "\"," +
                "\"id\": \"2\"," +
                "\"ext\": {" +
                "\"access_token\": \"" + ACCESS_TOKEN + "\"," +
                "\"timestamp\": " + System.currentTimeMillis() + "}}";

        buildGroupMeHttpRequestAndSend(payload);
    }

    private void subscribeGroup(String clientId){
        String payload = "{\"channel\": \"/meta/subscribe\"," +
                "\"clientId\": \"" + clientId + "\"," +
                "\"subscription\": \"/group/" + GROUP_ID + "\"," +
                "\"id\": \"3\"," +
                "\"ext\": {" +
                "\"access_token\": \"" + ACCESS_TOKEN + "\"," +
                "\"timestamp\": " + System.currentTimeMillis() + "}}";

        buildGroupMeHttpRequestAndSend(payload);
    }

    private void openWebSocket(String clientId){
        String payload = "{\"channel\": \"/meta/connect\"," +
                "\"clientId\": \"" + clientId + "\"," +
                "\"connectionType\": \"websocket\"," +
                "\"id\": \"4\"}";

        WebSocket.Builder webSocketBuilder = client.newWebSocketBuilder();
        WebSocket webSocket = webSocketBuilder.buildAsync(URI.create(WSS_PROTOCOL + GROUP_ME_URL), new WebSocketListener()).join();
        webSocket.sendText(payload, true);
    }

    private HttpResponse<String> buildGroupMeHttpRequestAndSend(String payload) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTPS_PROTOCOL + GROUP_ME_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class WebSocketListener implements Listener {
        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.println("onClose");
            return Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("Error occurred" + error.getMessage());
            Listener.super.onError(webSocket, error);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            try {
                JsonArray jsonArray = new JsonParser().parse(data.toString()).getAsJsonArray();
                ChatResponse r = gson.fromJson(jsonArray.get(0), ChatResponse.class);

                //if message was created by a user (not bot) in the required group,
                //then send to bot for processing
                if(r.getData().getSubject().getGroupId().equals(GROUP_ID) &&
                    r.getData().getSubject().getSenderType().equals("user")){
                    bot.processResponse(r);
                }
            } catch(Exception e){
                // do nothing
            }

            return Listener.super.onText(webSocket, data, last);
        }
    }
}
