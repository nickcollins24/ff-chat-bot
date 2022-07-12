package ncollins.chat.groupme;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import ncollins.chat.ChatBotListener;
import ncollins.model.chat.ChatResponse;
import ncollins.model.chat.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

@Component
@ConditionalOnProperty(value = "CHAT_LISTENER_ENABLED",
                       havingValue = "true")
public class GroupMeListener implements ChatBotListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String HTTPS_GROUP_ME_URL = "https://push.groupme.com/faye";
    private static final String WSS_GROUP_ME_URL = "wss://push.groupme.com/faye";
    private static final String GROUP_ME_ACCESS_TOKEN = System.getenv("GROUP_ME_ACCESS_TOKEN");

    private GroupMeProcessor processor;
    private HttpClient client = HttpClient.newHttpClient();

    public GroupMeListener(GroupMeProcessor processor){
        this.processor = processor;
        listen();
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
                "\"subscription\": \"/user/" + processor.getMainBot().getBotUserId() + "\"," +
                "\"id\": \"2\"," +
                "\"ext\": {" +
                "\"access_token\": \"" + GROUP_ME_ACCESS_TOKEN + "\"," +
                "\"timestamp\": " + System.currentTimeMillis() + "}}";

        buildGroupMeHttpRequestAndSend(payload);
    }

    private void subscribeGroup(String clientId){
        String payload = "{\"channel\": \"/meta/subscribe\"," +
                "\"clientId\": \"" + clientId + "\"," +
                "\"subscription\": \"/group/" + processor.getMainBot().getBotGroupId() + "\"," +
                "\"id\": \"3\"," +
                "\"ext\": {" +
                "\"access_token\": \"" + GROUP_ME_ACCESS_TOKEN + "\"," +
                "\"timestamp\": " + System.currentTimeMillis() + "}}";

        buildGroupMeHttpRequestAndSend(payload);
    }

    private void openWebSocket(String clientId){
        String payload = "{\"channel\": \"/meta/connect\"," +
                "\"clientId\": \"" + clientId + "\"," +
                "\"connectionType\": \"websocket\"," +
                "\"id\": \"4\"}";

        WebSocket.Builder webSocketBuilder = client.newWebSocketBuilder();
        WebSocket webSocket =
                webSocketBuilder.buildAsync(URI.create(WSS_GROUP_ME_URL), new GroupMeSocketListener(processor)).join();
        webSocket.sendText(payload, true);
    }

    private HttpResponse<String> buildGroupMeHttpRequestAndSend(String payload) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HTTPS_GROUP_ME_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            logger.error("Unexpected exception on websocket connection init: " + e
                    + ". Payload: " + payload + ". Reconnecting...");
            listen();
        }

        return null;
    }

    public class GroupMeSocketListener implements WebSocket.Listener {
        private GroupMeProcessor processor;
        private Gson gson;

        public GroupMeSocketListener(GroupMeProcessor processor){
            this.processor = processor;
            this.gson = new Gson();
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            logger.debug("onText: " + data);
            JsonArray jsonArray = new JsonParser().parse(data.toString()).getAsJsonArray();

            // if data contains successful attribute, then send a ping to let server know we're still alive
            if(jsonArray.get(0).getAsJsonObject().has("successful")){
                logger.debug("Reconnecting...");
                webSocket.abort();
                listen();
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }

            try {
                ChatResponse r = gson.fromJson(jsonArray.get(0), ChatResponse.class);
                Subject subject = r.getData().getSubject();

                processor.processResponse(subject, System.currentTimeMillis());
            } catch(JsonSyntaxException | NullPointerException e) {
                //do nothing
            } catch(Exception e){
                logger.error("Unexpected exception in onText: " + e);
            }

            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onOpen(WebSocket webSocket){
            logger.info("WebSocket connection opened.");
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            logger.info("WebSocket connection closed. Reconnecting...");
            webSocket.abort();
            listen();
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            logger.error("WebSocket error occurred: " + error.getMessage() + ". Reconnecting...");
            webSocket.abort();
            listen();
            WebSocket.Listener.super.onError(webSocket, error);
        }
    }
}
