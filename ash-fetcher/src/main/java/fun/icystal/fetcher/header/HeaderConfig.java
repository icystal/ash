package fun.icystal.fetcher.header;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HeaderConfig {

    /**
     * 这里保存着所有配置的 请求头信息
     */
    public static Map<URI, Map<String, String>> headers = initHeaders();


    private static Map<URI, Map<String, String>> initHeaders() {
        Map<String, String> rawHeaders = loadHeadersJsonAsMap();
        ConcurrentHashMap<URI, Map<String, String>> configHeaders = new ConcurrentHashMap<>();

        for (Map.Entry<String, String> entry : rawHeaders.entrySet()) {
            URI uri = URI.create(entry.getKey());
            Map<String, String> map = JSON.parseObject(entry.getValue(), new TypeReference<>() {});
            configHeaders.put(uri, map);
        }
        return configHeaders;
    }

    private static Map<String, String> loadHeadersJsonAsMap() {
        try (InputStream resource = HeaderConfig.class.getClassLoader().getResourceAsStream(CONFIG_JSON_PATH)) {
            if (resource == null) {
                return Collections.emptyMap();
            }
            String content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            return JSON.parseObject(content, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("load json resource from file failed, path: {}", CONFIG_JSON_PATH, e);
            return Collections.emptyMap();
        }
    }

    private static final String CONFIG_JSON_PATH = "fetcher/headers.json";

    public static void main(String[] args) {
        System.out.println("HeaderConfig init ...");
    }

}
