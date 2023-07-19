package ncollins.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ncollins.model.chat.slack.GetUserResponse;
import ncollins.model.chat.slack.SlackUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class SlackHttpClient {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private HttpClient client;
    private ObjectMapper mapper;
    private static final String SLACK_MAIN_AUTH_TOKEN = System.getenv("SLACK_MAIN_AUTH_TOKEN");
    private static final String GET_USER_URL = "https://slack.com/api/users.info";

    public SlackHttpClient(){
        this.client = HttpClient.newBuilder().build();
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SlackUser getUser(String userId){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GET_USER_URL + "?user=" + userId))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + SLACK_MAIN_AUTH_TOKEN)
                    .GET()
                    .build();

            HttpResponse<String> response = send(request, HttpResponse.BodyHandlers.ofString());
            GetUserResponse userResponse = mapper.readValue(response.body(), GetUserResponse.class);

            return userResponse != null ? userResponse.getUser() : null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler){
        try {
            return client.send(request, responseBodyHandler);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
