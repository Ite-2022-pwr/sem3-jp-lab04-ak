package ite.jp.ak.lab04.client.web.api;

import ite.jp.ak.lab04.client.config.Config;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static ApiClient instance;

    private ApiClient() {

    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    private <R> R makeRequest(HttpMethod method, String uri, Class<R> responseType) {
        return WebClient.builder().baseUrl(Config.SERVER_URL).build()
                .method(method)
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <R> R get(String uri, Class<R> responseType) {
        return makeRequest(HttpMethod.GET, uri, responseType);
    }
}
