package net.dunice.newsapi.configurations;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "endpoints")
public class EndpointsConfiguration {
    private String[] permittedEndpoints;
}
