package net.dunice.newsapi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    private static final String SLASH = "/";

    public static String withoutTrailingSlash(String url) {
        if (url.endsWith(SLASH)) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
