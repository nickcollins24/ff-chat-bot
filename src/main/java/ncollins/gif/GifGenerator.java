package ncollins.gif;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GifGenerator {
    private HttpClient client;
    private String giphyKey;
    private String giphyRating;
    private static final String GIPHY_URL = "https://api.giphy.com/v1/gifs/translate";

    public GifGenerator(String giphyKey, String giphyRating){
        this.giphyKey = giphyKey;
        this.giphyRating = giphyRating;
        this.client = HttpClient.newHttpClient();
    }

    public String getRandomGif(String query){
        String queryNoSpaces = query.replaceAll(" ", "+");
        URI giphyUrl = URI.create(GIPHY_URL + "?api_key=" + giphyKey + "&s=" + queryNoSpaces + "&rating=" + giphyRating);

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }
}
