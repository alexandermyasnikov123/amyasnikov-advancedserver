package net.dunice.newsapi.impl;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpServletResponseUtils {

    public static Optional<String> findCachedResponseBody(ServletResponse response) {
        ServletResponse current = response;

        while (current instanceof HttpServletResponseWrapper currentWrapper) {
            if (currentWrapper instanceof ContentCachingResponseWrapper cachingWrapper) {
                return Optional.of(new String(cachingWrapper.getContentAsByteArray()));
            }

            current = currentWrapper.getResponse();
        }

        return Optional.empty();
    }
}
