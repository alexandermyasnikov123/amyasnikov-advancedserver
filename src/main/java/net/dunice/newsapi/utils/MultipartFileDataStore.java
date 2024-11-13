package net.dunice.newsapi.utils;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public interface MultipartFileDataStore {
    URI compressAndStore(MultipartFile file) throws IOException;

    Resource loadCompressedFile(String filename) throws MalformedURLException;
}
