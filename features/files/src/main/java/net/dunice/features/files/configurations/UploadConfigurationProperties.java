package net.dunice.features.files.configurations;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfigurationProperties {
    private String pattern;

    private String dir;

    private String fileProtocol;

    private String imageExtension;

    private Double scaleFactor;

    private Integer maxAvailableSizeBytes;
}
