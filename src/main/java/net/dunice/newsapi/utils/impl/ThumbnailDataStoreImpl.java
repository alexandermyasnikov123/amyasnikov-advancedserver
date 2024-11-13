package net.dunice.newsapi.utils.impl;

import net.coobird.thumbnailator.Thumbnails;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.springframework.beans.factory.annotation.Value;
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
public class ThumbnailDataStoreImpl implements MultipartFileDataStore {
    @Value("${upload.pattern}")
    private String namePattern;

    @Value("${upload.file-protocol}")
    private String fileProtocol;

    @Value("${upload.image-extension}")
    private String imageExtension = ".jpg";

    @Value("${user.dir}/${upload.dir}")
    private String outputDirectoryPath;

    @Value("${upload.scale-factor}")
    private Double scaleFactor;

    @Value("${upload.max-available-size-bytes}")
    private Integer maxAvailableSizeBytes;

    @Override
    public URI compressAndStore(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(outputDirectoryPath));

        String fileSimpleName = new SimpleDateFormat(namePattern)
                .format(new Date()) + imageExtension;

        String filePath = "%s/%s".formatted(outputDirectoryPath, fileSimpleName);

        File output = new File(filePath);
        saveAndCompressFileIfAbsent(file, output);

        String outputUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .toUriString();

        return URI.create("%s/%s/%s".formatted(outputUrl, fileProtocol, fileSimpleName));
    }

    @Override
    public Resource loadCompressedFile(String filename) throws MalformedURLException {
        String filePath = "%s/%s".formatted(outputDirectoryPath, filename);
        return new UrlResource(fileProtocol, filePath);
    }

    private void saveAndCompressFileIfAbsent(MultipartFile file, File outputFile) throws IOException {
        if (file.getSize() < maxAvailableSizeBytes) {
            file.transferTo(outputFile);
            return;
        }

        Thumbnails.of(file.getInputStream())
                .scale(scaleFactor)
                .toFile(outputFile);
    }
}
