package ncollins.gif;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ncollins.clients.RetryableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GiphyGenerator implements GifGenerator {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String GIPHY_KEY = System.getenv("GIPHY_KEY");
    private static final String GIPHY_RATING = System.getenv("GIPHY_RATING");
    private static final String GIPHY_ENDPOINT = "https://api.giphy.com/v1/gifs";

    private RetryableHttpClient client;

    @Autowired
    public GiphyGenerator(RetryableHttpClient retryableHttpClient){
        this.client = retryableHttpClient;
    }

    public String translate(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(
                String.format("%s/translate?api_key=%s&s=%s&rating=%s", GIPHY_ENDPOINT, GIPHY_KEY, queryNoSpaces, GIPHY_RATING));

        ResponseEntity<String> response = client.get(giphyUrl);
        JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
        if(jsonObject.get("data").isJsonArray()) {
            return "i couldn't find a gif for that...";
        }

        return jsonObject.get("data").getAsJsonObject().
                get("images").getAsJsonObject().
                get("fixed_height").getAsJsonObject().
                get("url").getAsString();
    }

    @Override
    public String search(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(
                String.format("%s/search?api_key=%s&q=%s&rating=%s&limit=40", GIPHY_ENDPOINT, GIPHY_KEY, queryNoSpaces, GIPHY_RATING));

        ResponseEntity<String> response = client.get(giphyUrl);
        JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");

        if(jsonArray.size() == 0) {
            return "i couldn't find a gif for that...";
        }

        return jsonArray.get(ThreadLocalRandom.current().nextInt(0,jsonArray.size())).getAsJsonObject().
                getAsJsonObject("images").
                getAsJsonObject("fixed_height").
                get("url").getAsString();
    }
}
