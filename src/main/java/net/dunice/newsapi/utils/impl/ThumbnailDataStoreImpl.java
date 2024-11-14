package net.dunice.newsapi.utils.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.coobird.thumbnailator.Thumbnails;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import net.dunice.newsapi.utils.UploadProperties;
import net.dunice.newsapi.utils.UserProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThumbnailDataStoreImpl implements MultipartFileDataStore {
    UploadProperties uploadProperties;

    UserProperties userProperties;

    @Override
    public URI compressAndStore(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(getOutputPath()));

        String fileSimpleName = new SimpleDateFormat(uploadProperties.getPattern())
                .format(new Date()) + uploadProperties.getImageExtension();

        String filePath = "%s/%s".formatted(getOutputPath(), fileSimpleName);

        File output = new File(filePath);
        saveAndCompressFileIfAbsent(file, output);

        String outputUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .toUriString();

        return URI.create("%s/%s/%s".formatted(outputUrl, uploadProperties.getFileProtocol(), fileSimpleName));
    }

    @Override
    public Resource loadCompressedFile(String filename) throws MalformedURLException {
        String filePath = "%s/%s".formatted(getOutputPath(), filename);
        return new UrlResource(uploadProperties.getFileProtocol(), filePath);
    }

    private String getOutputPath() {
        return "%s/%s".formatted(userProperties.getDir(), uploadProperties.getDir());
    }

    private void saveAndCompressFileIfAbsent(MultipartFile file, File outputFile) throws IOException {
        if (file.getSize() < uploadProperties.getMaxAvailableSizeBytes()) {
            file.transferTo(outputFile);
            return;
        }

        Thumbnails.of(file.getInputStream())
                .scale(uploadProperties.getScaleFactor())
                .toFile(outputFile);
    }
}
