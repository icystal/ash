package fun.icystal.ash.crawler.http;


import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpRequestEmitter {

    HttpRequest request(URI target);

    void response(HttpResponse<String> response);

}
