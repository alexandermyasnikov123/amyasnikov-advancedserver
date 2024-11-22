package net.dunice.newsapi.configurations;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "endpoints")
public class EndpointsConfiguration {
    private String[] permittedAllEndpoints;

    private String[] permittedGetEndpoints;

    private List<String> allowedMethods;

    private List<String> allowedHeaders;

    private List<String> allowedOrigins;

    private String corsPattern;

    private List<String> loggingEndpoints;
}
