package fun.icystal.ash.crawler.http;

import lombok.NonNull;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRequestEmitter implements HttpRequestEmitter{

    private final Map<String, String> headers;

    public SimpleRequestEmitter(@NonNull Map<String, String> headers, boolean cookieIgnored) {
        Map<String, String> customHeaders = new ConcurrentHashMap<>(headers);
        if (cookieIgnored) {
            customHeaders.remove("Cookie");
        }
        this.headers = customHeaders;
    }

    @Override
    public HttpRequest request(URI target) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(target)
                .timeout(Duration.ofSeconds(10))
                .headers(getHeaders())
                .build();
    }

    @Override
    public void response(HttpResponse<String> response) {

    }

    public String[] getHeaders() {
        List<String> headerList = new ArrayList<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerList.add(entry.getKey());
            headerList.add(entry.getValue());
        }
        return headerList.toArray(new String[0]);
    }
}
