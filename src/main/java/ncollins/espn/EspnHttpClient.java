package ncollins.espn;

import org.springframework.stereotype.Component;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class EspnHttpClient {
    private static final String ESPN_S2_COOKIE = System.getenv("ESPN_S2_COOKIE");

    private HttpClient client;

    public EspnHttpClient(){
        CookieStore cookieStore = (new CookieManager()).getCookieStore();

        HttpCookie espnS2Cookie = new HttpCookie("espn_s2", ESPN_S2_COOKIE);
        espnS2Cookie.setPath("/");
        espnS2Cookie.setDomain(".espn.com");
        cookieStore.add(null, espnS2Cookie);

        CookieManager cookieManager = new CookieManager(cookieStore, null);
        this.client = HttpClient.newBuilder().cookieHandler(cookieManager).build();
    }

    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler){
        try {
            return client.send(request, responseBodyHandler);
        } catch (Exception e) {
            return null;
        }
    }
}
