package fun.icystal.ash.crawler.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class Loader {

    public static Map<String, String> loadJsonAsMap(String path) {
        try (InputStream resource = Loader.class.getClassLoader().getResourceAsStream(path)) {
            if (resource == null) {
                return Collections.emptyMap();
            }
            String content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            return JSON.parseObject(content, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("load json resource from file failed, path: {}", path, e);
            return Collections.emptyMap();
        }
    }

}
