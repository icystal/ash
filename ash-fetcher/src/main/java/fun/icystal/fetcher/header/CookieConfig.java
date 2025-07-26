package fun.icystal.fetcher.header;

import org.springframework.util.CollectionUtils;

import java.net.*;
import java.util.Arrays;
import java.util.Map;

public class CookieConfig {

    public static final CookieManager cookieManager = initCookie();

    private static CookieManager initCookie() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        for (Map.Entry<URI, Map<String, String>> entry : HeaderConfig.headers.entrySet()) {
            URI target = entry.getKey();

            Map<String, String> headers = entry.getValue();
            if (CollectionUtils.isEmpty(headers) || !headers.containsKey(COOKIE)) {
                continue;
            }

            addCookie(target, headers.get(COOKIE), cookieManager.getCookieStore());
        }
        return cookieManager;
    }

    private static void addCookie(URI target, String rawCookies, CookieStore store) {
        if (rawCookies == null || rawCookies.isBlank()) {
            return;
        }

        Arrays.stream(rawCookies.split(";"))
                .map(String::strip)
                .map(rawCookie -> {
                int index = rawCookie.strip().indexOf('=');
                HttpCookie cookie = new HttpCookie(rawCookie.substring(0, index).strip(), rawCookie.substring(index + 1).strip());
                cookie.setPath(target.getPath());
                cookie.setDomain(target.getAuthority());
                return cookie;
            })
                .forEach(cookie -> store.add(target, cookie));
    }

    private static final String COOKIE = "Cookie";


    public static void main(String[] args) {
        System.out.println("CookieConfig init ...");
    }

}
