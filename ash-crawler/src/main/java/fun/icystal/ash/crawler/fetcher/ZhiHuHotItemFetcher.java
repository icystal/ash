package fun.icystal.ash.crawler.fetcher;

import fun.icystal.ZhiHuHotItem;
import fun.icystal.ash.crawler.exception.FetchFailedException;
import fun.icystal.ash.crawler.http.HeaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 抓取知乎热榜的工具
 */
@Slf4j
public class ZhiHuHotItemFetcher {

    private final HttpClient client;

    private final URI targetUri;

    private final HeaderFactory headerFactory;

    private static final String zhiHuHotRankUrl = "https://www.zhihu.com/hot";

    private ZhiHuHotItemFetcher() {

        targetUri = URI.create(zhiHuHotRankUrl);
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        headerFactory = HeaderFactory.headers();
    }


    List<ZhiHuHotItem> fetch() throws IOException, InterruptedException, FetchFailedException {

        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(Charset.defaultCharset());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(targetUri)
                .timeout(Duration.ofSeconds(10))
                .headers(headerFactory.getHeaders())
                .build();
        HttpResponse<String> response = client.send(request, bodyHandler);

        if (response.statusCode() != 200) {
            throw new FetchFailedException();
        }
        headerFactory.acceptCookies(response.headers());

        String body = response.body();

        Document document = Jsoup.parse(body);
        return select(document);
    }

    List<ZhiHuHotItem> fetch(int retry) {
        if (retry < 0) {
            return Collections.emptyList();
        }
        try {
            return fetch();
        } catch (Exception e) {
            sleep();
            return fetch(retry - 1);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException ignored) {}
    }

    private List<ZhiHuHotItem> select(Document document) {
        LocalDateTime time = LocalDateTime.now();
        Elements hotItems = document.getElementsByClass("HotItem");

        List<ZhiHuHotItem> hotItemList = new ArrayList<>();
        for (Element hotItem : hotItems) {
            ZhiHuHotItem zhiHuHotItem = new ZhiHuHotItem();
            zhiHuHotItem.setIndex(selectIndex(hotItem));
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
            log.error("select title error from hot item: {}", hotItem.toString());
        }
        return null;
    }

    private String selectLink(Element hotItem) {
        try {
            return Objects.requireNonNull(hotItem.getElementsByClass("HotItem-content").getFirst().getElementsByTag("a").getFirst().attribute("href")).getValue();
        } catch (Exception e) {
            log.error("select link error from hot item: {}", hotItem.toString());
        }
        return null;
    }

    private String selectText(Element hotItem) {
        try {
            return hotItem.getElementsByClass("HotItem-excerpt").getFirst().text();
        } catch (Exception e) {
            log.error("select text error from hot item: {}", hotItem.toString());
        }
        return null;
    }

    private String selectImg(Element hotItem) {
        try {
            return Objects.requireNonNull(hotItem.getElementsByClass("HotItem-img").getFirst()
                    .getElementsByTag("img").getFirst()
                    .attribute("src")).getValue();
        } catch (Exception e) {
            log.error("select image error from hot item: {}", hotItem.toString());
        }
        return null;
    }

    private Integer selectIndex(Element hotItem) {
        try {
            String index = hotItem.getElementsByClass("HotItem-rank").getFirst().text();
            return Integer.parseInt(index);
        } catch (Exception e) {
            log.error("select index error from hot item: {}", hotItem.toString());
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ZhiHuHotItemFetcher fetcher = new ZhiHuHotItemFetcher();
        List<ZhiHuHotItem> hotItems = fetcher.fetch(3);
        System.out.println(hotItems);
    }


}
