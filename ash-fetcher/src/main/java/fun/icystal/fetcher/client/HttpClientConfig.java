package fun.icystal.fetcher.client;

import fun.icystal.fetcher.header.CookieConfig;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

public class HttpClientConfig {

    public static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .cookieHandler(CookieConfig.cookieManager)
            .build();

}
