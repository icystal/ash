package fun.icystal.ash.crawler.fetcher;

import fun.icystal.ash.crawler.http.HttpRequestEmitter;
import fun.icystal.exception.FetchFailedException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Fetcher<T> {

    public static final int HTTP_RESPONSE_STATUS_OK = 200;

    protected HttpClient client;

    protected URI target;

    protected HttpRequestEmitter emitter;

    protected HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

    protected Fetcher(String url) {
        target = URI.create(url);
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(initCookieHandler())
                .build();
        emitter = initEmitter();
    }

    protected abstract CookieHandler initCookieHandler();

    protected abstract HttpRequestEmitter initEmitter();


    protected T fetch() throws IOException, InterruptedException, FetchFailedException {
        HttpRequest request = emitter.request(target);
        HttpResponse<String> response = client.send(request, bodyHandler);
        if (response.statusCode() != HTTP_RESPONSE_STATUS_OK) {
            throw new FetchFailedException();
        }
        emitter.response(response);
        return select(response);
    }

    public T fetch(int retry) {
        if (retry < 0) {
            return null;
        }
        try {
            return fetch();
        } catch (Exception e) {
            interval();
            return fetch(retry - 1);
        }
    }

    protected void interval() {
        try {
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException ignored) {
        }
    }

    protected abstract T select(HttpResponse<String> response);
}
