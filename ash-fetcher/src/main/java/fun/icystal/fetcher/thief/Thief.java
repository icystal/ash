package fun.icystal.fetcher.thief;

import fun.icystal.fetcher.client.HttpClientConfig;
import fun.icystal.fetcher.header.HeaderHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Thief {

    private static final HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

    public static Document execute(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .headers(HeaderHandler.getHeaders(uri))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = HttpClientConfig.client.send(request, bodyHandler);
        if (response.statusCode() != 200) {
            throw new RuntimeException("Thief returned " + response.statusCode());
        }
        return Jsoup.parse(response.body());
    }

    public static Document execute(URI uri, int retry) {
        try {
            for (int i = 0; i <= retry; i++) {
                try {
                    return execute(uri);
                } catch (Exception ignored) {
                    Thread.sleep(5 + 10L * retry);
                }
            }
        } catch (Exception ignored) {
            execute(uri, retry - 1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Document response = Thief.execute(URI.create("https://www.zhihu.com/hot"));
        System.out.println(response);

    }

}
