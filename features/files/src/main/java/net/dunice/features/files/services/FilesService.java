package net.dunice.features.files.services;

import jakarta.servlet.http.HttpServletRequest;
import net.dunice.features.core.dtos.responses.common.BaseSuccessResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;

public interface FilesService {
    BaseSuccessResponse storeFile(MultipartFile file, HttpServletRequest request) throws Exception;

    Resource loadFile(String filename) throws MalformedURLException;

    void deleteFileByUrl(String url);
}
