package fun.icystal.ash.crawler.fetcher;

import com.alibaba.fastjson2.JSON;
import fun.icystal.ash.crawler.util.Loader;
import fun.icystal.entity.ZhiHuHotItem;
import fun.icystal.ash.crawler.http.HttpRequestEmitter;
import fun.icystal.ash.crawler.http.SimpleRequestEmitter;
import fun.icystal.exception.FetchFailedException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class ZhiHuHotItemFetcher extends Fetcher<List<ZhiHuHotItem>> {
    private static final String resourcePath = "crawler/zh_headers.json";

    private static final String url = "https://www.zhihu.com/hot";

    public ZhiHuHotItemFetcher() {
        super(url);
    }

    @Override
    protected CookieHandler initCookieHandler() {
        Map<String, String> headers = Loader.loadJsonAsMap(resourcePath);

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        CookieStore store = manager.getCookieStore();
        parseCookies(headers.get("Cookie")).forEach(cookie -> store.add(target, cookie));
        return manager;
    }

    private List<HttpCookie> parseCookies(String rawCookies) {
        if (rawCookies == null || rawCookies.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(rawCookies.split(";"))
                .map(String::strip)
                .map(rawCookie -> {
                    int index = rawCookie.strip().indexOf('=');
                    HttpCookie cookie = new HttpCookie(rawCookie.substring(0, index).strip(), rawCookie.substring(index + 1).strip());
                    cookie.setPath(target.getPath());
                    cookie.setDomain(target.getAuthority());
                    return cookie;
                }).toList();
    }

    @Override
    protected HttpRequestEmitter initEmitter() {
        Map<String, String> headers = Loader.loadJsonAsMap(resourcePath);
        return new SimpleRequestEmitter(headers, true);
    }

    @Override
    protected List<ZhiHuHotItem> select(HttpResponse<String> response) {
        Document document = Jsoup.parse(response.body());
        LocalDateTime time = LocalDateTime.now();
        Elements hotItems = document.getElementsByClass("HotItem");

        List<ZhiHuHotItem> hotItemList = new ArrayList<>();
        for (Element hotItem : hotItems) {
            ZhiHuHotItem zhiHuHotItem = new ZhiHuHotItem();
            zhiHuHotItem.setSort(selectIndex(hotItem));
            zhiHuHotItem.setLink(selectLink(hotItem));
            zhiHuHotItem.setTitle(selectTitle(hotItem));
            zhiHuHotItem.setExcerpt(selectText(hotItem));
            zhiHuHotItem.setImgUrl(selectImg(hotItem));
            zhiHuHotItem.setFetchTime(time);

            hotItemList.add(zhiHuHotItem);
        }
        return hotItemList;
    }

    private String selectTitle(Element hotItem) {
        try {
            return hotItem.getElementsByClass("HotItem-title").getFirst().text();
        } catch (Exception e) {
            log.error("select title error from hot item");
        }
        return null;
    }

    private String selectLink(Element hotItem) {
        try {
            return Objects.requireNonNull(hotItem.getElementsByClass("HotItem-content").getFirst().getElementsByTag("a").getFirst().attribute("href")).getValue();
        } catch (Exception e) {
            log.error("select link error from hot item");
        }
        return null;
    }

    private String selectText(Element hotItem) {
        try {
            return hotItem.getElementsByClass("HotItem-excerpt").getFirst().text();
        } catch (Exception e) {
            log.error("select text error from hot item");
        }
        return null;
    }

    private String selectImg(Element hotItem) {
        try {
            return Objects.requireNonNull(hotItem.getElementsByClass("HotItem-img").getFirst()
                    .getElementsByTag("img").getFirst()
                    .attribute("src")).getValue();
        } catch (Exception e) {
            log.error("select image error from hot item");
        }
        return null;
    }

    private Integer selectIndex(Element hotItem) {
        try {
            String index = hotItem.getElementsByClass("HotItem-rank").getFirst().text();
            return Integer.parseInt(index);
        } catch (Exception e) {
            log.error("select index error from hot item");
        }
        return null;
    }

    public static void main(String[] args) throws FetchFailedException, IOException, InterruptedException {
        Fetcher<List<ZhiHuHotItem>> fetcher = new ZhiHuHotItemFetcher();
        List<ZhiHuHotItem> items = fetcher.fetch(3);
        System.out.println(JSON.toJSONString(items));
    }

}
