package net.dunice.newsapi.services;

import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;

public interface FilesService {
    BaseSuccessResponse storeFile(MultipartFile file, String baseApiPath) throws Exception;

    Resource loadFile(String filename) throws MalformedURLException;
}
