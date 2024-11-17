package net.dunice.newsapi.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt.data")
public class JwtConfiguration {
    String usernameClaim;

    String roleClaim;

    String uuidClaim;

    Integer expirationMinutes;

    String bearerSecret;
}

