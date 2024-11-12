package net.dunice.newsapi.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;

@FunctionalInterface
public interface MultipartFileDataStore {
    URI compressAndStore(MultipartFile file) throws IOException;
}
