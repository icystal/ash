package fun.icystal.ash.crawler.fetcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import fun.icystal.ash.crawler.http.HttpRequestEmitter;
import fun.icystal.ash.crawler.http.SimpleRequestEmitter;
import fun.icystal.ash.crawler.util.Loader;
import fun.icystal.entity.CaiLianHeadline;
import fun.icystal.exception.FetchFailedException;
import jdk.jfr.Timestamp;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class CaiLianHeadlineFetcher extends Fetcher<List<CaiLianHeadline>>{

    private static final String url = "https://www.cls.cn/v3/depth/home/assembled/1000?app=CailianpressWeb&os=web&sv=8.4.6&sign=9f8797a1f4de66c2370f7a03990d2737";

    private static final String headerPath = "crawler/cls_headers.json";

    private static final String detailPrefix = "https://www.cls.cn/detail/";

    protected CaiLianHeadlineFetcher() {
        super(url);
    }

    @Override
    protected CookieHandler initCookieHandler() {
        Map<String, String> headers = Loader.loadJsonAsMap(headerPath);

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
                    cookie.setPath("/");
                    cookie.setDomain(target.getAuthority());
                    return cookie;
                }).toList();
    }

    @Override
    protected HttpRequestEmitter initEmitter() {
        return new SimpleRequestEmitter(Loader.loadJsonAsMap(headerPath), true);
    }

    @Override
    protected List<CaiLianHeadline> select(HttpResponse<String> response) {
        JSONObject resultObj = JSON.parseObject(response.body());
        JSONObject dataObj = (JSONObject) resultObj.get("data");
        JSONArray topArticleObjList = (JSONArray) dataObj.get("top_article");

        List<CaiLianHeadline> results = new ArrayList<>(topArticleObjList.size());
        for (Object topArticleObj : topArticleObjList) {
            JSONObject articleJson = (JSONObject) topArticleObj;
            CaiLianHeadline headline = new CaiLianHeadline();
            headline.setTitle(articleJson.get("title").toString());
            if (articleJson.containsKey("brief")) {
                headline.setBrief(articleJson.get("brief").toString());
            }
            headline.setLink(detailPrefix + articleJson.get("id"));
            if (articleJson.containsKey("ctime")) {
                long ctime = Long.parseLong(articleJson.get("ctime").toString());
                LocalDateTime publicationTime = LocalDateTime.ofEpochSecond(ctime, 0, ZoneOffset.of("+8"));
                headline.setPublicationTime(publicationTime);
            }
            results.add(headline);
        }

        return results;
    }

    public static void main(String[] args) throws FetchFailedException, IOException, InterruptedException {
        CaiLianHeadlineFetcher fetcher = new CaiLianHeadlineFetcher();
        List<CaiLianHeadline> caiLianHeadlines = fetcher.fetch();
        System.out.println(caiLianHeadlines);
    }
}
