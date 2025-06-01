package fun.icystal.ash.crawler.http;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class CookieFactory {

    private final Map<String, String> cookies;

    public String getCookie() {
        return cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("; "));
    }

    public void updateCookies(String rawCookies) {
        if (rawCookies == null || rawCookies.isBlank()) {
            return;
        }
        Arrays.stream(rawCookies.split(";"))
                .map(String::strip)
                .forEach(this::updateCookie);
    }

    private void updateCookie(String rawCookie) {
        try {
            int index = rawCookie.strip().indexOf('=');
            cookies.put(rawCookie.substring(0, index).strip(), rawCookie.substring(index + 1).strip());
        } catch (Exception e) {
            log.error("update cookie error, raw cookie string: {}", rawCookie);
        }
    }

    public CookieFactory() {
        cookies = new ConcurrentHashMap<>();
    }

    public static CookieFactory initCookies(String cookies) {
        CookieFactory cookieFactory = new CookieFactory();
        cookieFactory.updateCookies(cookies);
        return cookieFactory;
    }
}
