package net.dunice.newsapi.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "upload")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadConfigurationProperties {
    String pattern;

    String dir;

    String fileProtocol;

    String imageExtension;

    Double scaleFactor;

    Integer maxAvailableSizeBytes;
}
