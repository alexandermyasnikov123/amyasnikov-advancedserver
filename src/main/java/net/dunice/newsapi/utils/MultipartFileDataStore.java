package net.dunice.newsapi.utils;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;

public interface MultipartFileDataStore {
    String compressAndStore(MultipartFile file, String baseApiPath) throws IOException;

    Resource loadCompressedFile(String filename) throws MalformedURLException;

    @SuppressWarnings("UnusedReturnValue")
    Boolean deleteFileByName(String name);
}
