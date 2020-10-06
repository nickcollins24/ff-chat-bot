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

public class TenorGenerator implements GifGenerator{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TENOR_KEY = "W157AV87YE91"; //System.getenv("TENOR_KEY");
    private static final String TENOR_ENDPOINT = "https://api.tenor.com/v1";

    private HttpClient client;

    public TenorGenerator(){
        this.client = HttpClient.newHttpClient();
    }

    @Override
    public String search(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(TENOR_ENDPOINT + "/search?key=" + TENOR_KEY + "&q=" + queryNoSpaces + "&media_filter=minimal&limit=40");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(giphyUrl)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("results");

            if(jsonArray.size() == 0) {
                return "i couldn't find a gif for that...";
            }

            return jsonArray.get(ThreadLocalRandom.current().nextInt(0,jsonArray.size())).getAsJsonObject().
                    getAsJsonArray("media").get(0).getAsJsonObject().
                    getAsJsonObject("gif").
                    get("url").getAsString();
        } catch (IOException | InterruptedException e) {
            logger.error("Exception while getting gif for query (" + query + "): " + e);
        }

        logger.error("Failed to get gif for query: " + query);
        return "i couldn't find a gif for that...";
    }
}
