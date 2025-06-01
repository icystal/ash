package fun.icystal.ash.crawler.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class HeaderFactory {

    private Map<String, String> headers;

    private CookieFactory cookieFactory;


    public String[] getHeaders() {
        List<String> headerList = new ArrayList<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerList.add(entry.getKey());
            headerList.add(entry.getValue());
        }

        headerList.add("Cookie");
        headerList.add(cookieFactory.getCookie());
        return headerList.toArray(new String[0]);
    }

    public void acceptCookies(HttpHeaders httpHeaders) {
        cookieFactory.updateCookies(httpHeaders.firstValue("Cookie").orElse(null));
    }

    public static HeaderFactory headers() {
        Map<String, String> initHeaders = loadResource();

        HeaderFactory headerFactory = new HeaderFactory();
        headerFactory.cookieFactory = CookieFactory.initCookies(initHeaders.get("Cookie"));
        headerFactory.headers = new ConcurrentHashMap<>();

        initHeaders.entrySet().stream()
                .filter(entry -> !"Cookie".equals(entry.getKey()))
                .forEach(entry -> headerFactory.headers.put(entry.getKey(), entry.getValue()));
        return headerFactory;
    }

    private static Map<String, String> loadResource() {
        try (InputStream resource = HeaderFactory.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resource == null) {
                return Collections.emptyMap();
            }
            String content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            return JSON.parseObject(content, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            log.error("load headers from resource file failed");
            return Collections.emptyMap();
        }
    }

    private static final String resourcePath = "crawler/headers.json";

    public static void main(String[] args) {
        HeaderFactory headers1 = headers();
        System.out.println(headers1);

    }
}
