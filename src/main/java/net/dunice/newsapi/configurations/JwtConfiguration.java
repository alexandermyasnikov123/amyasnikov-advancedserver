package net.dunice.newsapi.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt.data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtConfiguration {
    String usernameClaim;

    String roleClaim;

    String uuidClaim;

    Integer expirationMinutes;

    String bearerSecret;
}

