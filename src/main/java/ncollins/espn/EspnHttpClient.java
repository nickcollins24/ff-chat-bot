package ncollins.espn;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EspnHttpClient {
    private static final String ESPN_S2_COOKIE = "AEA4zGWW742Nu%2Bukdo3xanjijf5TxJkGbhoCjBLqoJopF6MC9rlfzkcR3jbQdabP6ADwwwZwEE7XIHkJ8Tv0q1S7TNxcKHW0goamY4xhnvQGSFBsFXy9Y%2FMyHGr%2BeRrzCkza%2FtRNv60QjusWqHDUQpugB8lWrcanlDXl0zpDoXRBd2mEUQIab4dzUpwBZuImi%2FqoEerLkibucZ60okobcxL6jXtdBI%2BX%2BzSw%2BvDngwxbwDR6SKFiTgK7f1fy%2F%2B4nlf%2BddtFPlg02cVVdI8leQ7nL";
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
