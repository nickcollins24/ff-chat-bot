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

@Primary
@Component
public class TenorGenerator implements GifGenerator{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TENOR_KEY = System.getenv("TENOR_KEY");
    private static final String TENOR_ENDPOINT = "https://api.tenor.com/v1";

    private RetryableHttpClient client;

    @Autowired
    public TenorGenerator(RetryableHttpClient retryableHttpClient){
        this.client = retryableHttpClient;
    }

    @Override
    public String search(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(
                String.format("%s/search?key=%s&q=%s&media_filter=minimal&limit=40", TENOR_ENDPOINT, TENOR_KEY, queryNoSpaces));

        ResponseEntity<String> response = client.get(giphyUrl);
        JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("results");

        if(jsonArray.size() == 0) {
            return "i couldn't find a gif for that...";
        }

        return jsonArray.get(ThreadLocalRandom.current().nextInt(0,jsonArray.size())).getAsJsonObject().
                getAsJsonArray("media").get(0).getAsJsonObject().
                getAsJsonObject("gif").
                get("url").getAsString();
    }
}
