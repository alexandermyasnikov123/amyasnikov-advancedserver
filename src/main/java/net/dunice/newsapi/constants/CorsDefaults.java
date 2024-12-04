package net.dunice.newsapi.constants;

import org.springframework.http.HttpMethod;
import java.util.List;
import java.util.stream.Stream;

public interface CorsDefaults {
    List<String> ALLOWED_ORIGINS = List.of("https://news-feed.dunice-testing.com");

    List<String> ALLOWED_METHODS = Stream.of(
            HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE
    ).map(HttpMethod::name).toList();

    List<String> ALLOWED_HEADERS = List.of("*");

    String PATTERN = "/**";
}
