package ncollins.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class RetryableHttpClient {
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("rawtypes")
    public final ResponseEntity<String> get(URI uri) {
        return get(uri, new HttpEntity<>(new HttpHeaders()));
    }

    @SuppressWarnings("rawtypes")
    public final ResponseEntity<String> get(URI uri, HttpEntity httpEntity) {
        ResponseEntity<String> responseEntity = retryTemplate
                .execute(context -> restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class));

        return responseEntity;
    }

}
