package net.dunice.newsapi.services.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dunice.newsapi.constants.ErrorCodes;
import net.dunice.newsapi.dtos.responses.common.BaseSuccessResponse;
import net.dunice.newsapi.dtos.responses.common.CustomSuccessResponse;
import net.dunice.newsapi.errors.ErrorCodesException;
import net.dunice.newsapi.services.FilesService;
import net.dunice.newsapi.utils.MultipartFileDataStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.nio.file.Paths;

@Slf4j
@Service
@AllArgsConstructor
public class FilesServiceImpl implements FilesService {
    private final MultipartFileDataStore dataStore;

    @Override
    public BaseSuccessResponse storeFile(MultipartFile file, String baseApiPath) throws Exception {
        if (file.isEmpty()) {
            throw new ErrorCodesException(ErrorCodes.EMPTY_MULTIPART_FILE);
        }

        String resultingPathUrl = dataStore.compressAndStore(file, baseApiPath);
        return new CustomSuccessResponse<>(resultingPathUrl);
    }

    @Override
    public Resource loadFile(String filename) throws MalformedURLException {
        return dataStore.loadCompressedFile(filename);
    }

    @Override
    public void deleteFileByUrl(String url) {
        String fileName = Paths.get(url).getFileName().toString();
        dataStore.deleteFileByName(fileName);
    }
}
