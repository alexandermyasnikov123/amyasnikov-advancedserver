package net.dunice.newsapi.utils.impl;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.dunice.newsapi.configurations.UploadConfigurationProperties;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ThumbnailDataStoreImpl implements MultipartFileDataStore {
    @Value(value = "${user.dir}")
    private final String userDirectory;

    private final UploadConfigurationProperties uploadProperties;

    @Override
    public String compressAndStore(MultipartFile file, String baseApiPath) throws IOException {
        Files.createDirectories(Paths.get(getOutputPath()));

        String fileSimpleName = new SimpleDateFormat(uploadProperties.getPattern())
                .format(new Date()) + uploadProperties.getImageExtension();

        String filePath = "%s/%s".formatted(getOutputPath(), fileSimpleName);

        File output = new File(filePath);
        saveAndCompressFileIfAbsent(file, output);

        return "%s/%s/%s".formatted(baseApiPath, uploadProperties.getFileProtocol(), fileSimpleName);
    }

    @Override
    public Resource loadCompressedFile(String filename) throws MalformedURLException {
        String filePath = "%s/%s".formatted(getOutputPath(), filename);
        return new UrlResource(uploadProperties.getFileProtocol(), filePath);
    }

    @Override
    public Boolean deleteFileByName(String name) {
        String path = "%s/%s".formatted(getOutputPath(), name);
        File file = new File(path);
        return file.delete();
    }

    private String getOutputPath() {
        return "%s/%s".formatted(userDirectory, uploadProperties.getDir());
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
