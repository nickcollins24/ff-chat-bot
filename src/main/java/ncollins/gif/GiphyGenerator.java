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

public class GiphyGenerator implements GifGenerator{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GIPHY_KEY = System.getenv("GIPHY_KEY");
    private static final String GIPHY_RATING = System.getenv("GIPHY_RATING");
    private static final String GIPHY_ENDPOINT = "https://api.giphy.com/v1/gifs";

    private HttpClient client;

    public GiphyGenerator(){
        this.client = HttpClient.newHttpClient();
    }

    public String translate(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(GIPHY_ENDPOINT + "/translate?api_key=" + GIPHY_KEY + "&s=" + queryNoSpaces + "&rating=" + GIPHY_RATING);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(giphyUrl)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            if(jsonObject.get("data").isJsonArray()) {
                return "i couldn't find a gif for that...";
            }

            return jsonObject.get("data").getAsJsonObject().
                    get("images").getAsJsonObject().
                    get("fixed_height").getAsJsonObject().
                    get("url").getAsString();
        } catch (IOException | InterruptedException e) {
            logger.error("Exception while getting gif for query (" + query + "): " + e);
        }

        logger.error("Failed to get gif for query: " + query);
        return "i couldn't find a gif for that...";
    }

    @Override
    public String search(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(GIPHY_ENDPOINT + "/search?api_key=" + GIPHY_KEY + "&q=" + queryNoSpaces + "&rating=" + GIPHY_RATING + "&limit=40");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(giphyUrl)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");

            if(jsonArray.size() == 0) {
                return "i couldn't find a gif for that...";
            }

            return jsonArray.get(ThreadLocalRandom.current().nextInt(0,jsonArray.size())).getAsJsonObject().
                    getAsJsonObject("images").
                    getAsJsonObject("fixed_height").
                    get("url").getAsString();
        } catch (IOException | InterruptedException e) {
            logger.error("Exception while getting gif for query (" + query + "): " + e);
        }

        logger.error("Failed to get gif for query: " + query);
        return "i couldn't find a gif for that...";
    }
}
