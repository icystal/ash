package fun.icystal.fetcher.header;

import java.net.URI;
import java.util.*;

public class HeaderHandler {

    public static String[] getHeaders(URI uri) {
        String path = uri.getPath();
        if (path == null || path.isEmpty()) {
            path = "/";
        }

        List<Map.Entry<URI, Map<String, String>>> validHeaderConfigs = new ArrayList<>();
        for (Map.Entry<URI, Map<String, String>> headerConfig : HeaderConfig.headers.entrySet()) {
            URI configUri = headerConfig.getKey();
            if (pathMatches(path, configUri.getPath())) {
                validHeaderConfigs.add(headerConfig);
            }
        }
        validHeaderConfigs.sort(Comparator.comparingInt(entry -> entry.getKey().getPath().length()));

        List<String> headers = new ArrayList<>();
        for (Map.Entry<URI, Map<String, String>> headerConfig : validHeaderConfigs) {
            Map<String, String> validHeader = headerConfig.getValue();
            for (Map.Entry<String, String> headerEntry : validHeader.entrySet()) {
                if (ignoredHeader.contains(headerEntry.getKey())) {
                    continue;
                }
                headers.add(headerEntry.getKey());
                headers.add(headerEntry.getValue());
            }
        }
        return headers.toArray(new String[0]);
    }

    private static boolean pathMatches(String path, String pathToMatchWith) {
        if (Objects.equals(path, pathToMatchWith))
            return true;
        if (path == null || pathToMatchWith == null)
            return false;
        return path.startsWith(pathToMatchWith);
    }

    private static final Set<String> ignoredHeader = new HashSet<>();
    static {
        ignoredHeader.add("Cookie");
        ignoredHeader.add("Cookie2");
        ignoredHeader.add("Set-Cookie");
        ignoredHeader.add("Set-Cookie2");
    }

}
