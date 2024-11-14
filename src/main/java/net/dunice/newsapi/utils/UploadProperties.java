package net.dunice.newsapi.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "upload")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadProperties {
    String pattern;

    String dir;

    String fileProtocol;

    String imageExtension;

    Double scaleFactor;

    Integer maxAvailableSizeBytes;
}
