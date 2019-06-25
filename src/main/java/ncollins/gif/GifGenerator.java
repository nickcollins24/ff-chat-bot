package ncollins.gif;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ThreadLocalRandom;

public class GifGenerator {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GIPHY_KEY = "RREB060E8fcRzgHRV8BM9xYqsYFdqB20";
    private static final String GIPHY_RATING = "R";
    private HttpClient client;

    public GifGenerator(){
        this.client = HttpClient.newHttpClient();
    }

    public String translateGif(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create("https://api.giphy.com/v1/gifs/translate?api_key=" + GIPHY_KEY + "&s=" + queryNoSpaces + "&rating=" + GIPHY_RATING);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(giphyUrl)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            return jsonObject.get("data").getAsJsonObject().
                    get("images").getAsJsonObject().
                    get("fixed_height").getAsJsonObject().
                    get("url").getAsString();
        } catch (IOException | InterruptedException e) {
            logger.error("Exception while getting gif for query (" + query + "): " + e);
        }

        logger.error("Failed to get gif for query: " + query);
        return "";
    }

    public String searchGif(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create("https://api.giphy.com/v1/gifs/search?api_key=" + GIPHY_KEY + "&q=" + queryNoSpaces + "&rating=" + GIPHY_RATING);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(giphyUrl)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            return jsonArray.get(ThreadLocalRandom.current().nextInt(0,jsonArray.size())).getAsJsonObject().
                    get("images").getAsJsonObject().
                    get("fixed_height").getAsJsonObject().
                    get("url").getAsString();
        } catch (IOException | InterruptedException e) {
            logger.error("Exception while getting gif for query (" + query + "): " + e);
        }

        logger.error("Failed to get gif for query: " + query);
        return "";
    }
}
